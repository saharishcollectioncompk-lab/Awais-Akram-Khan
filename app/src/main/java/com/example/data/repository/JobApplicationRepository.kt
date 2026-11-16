package com.example.data.repository

import com.example.data.firebase.OperationType
import com.example.data.firebase.handleFirestoreError
import com.example.data.model.JobApplication
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest

class JobApplicationRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth = Firebase.auth
) {
    private fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("Please sign in with Google before submitting an application.")
    }

    fun observeApplications(userId: String): Flow<List<JobApplication>> = flow {
        val collectionRef = db.collection("users").document(userId).collection("job_applications")
        emitAll(
            collectionRef.snapshots().map { snapshot ->
                snapshot.toObjects(
                    JobApplication::class.java,
                    DocumentSnapshot.ServerTimestampBehavior.ESTIMATE
                ).sortedByDescending { it.createdAt?.seconds ?: 0L }
            }
        )
    }

    suspend fun submitApplication(
        fullName: String,
        email: String,
        mobileNumber: String,
        cnicNumber: String,
        position: String,
        course: String,
        customCourse: String?
    ): Result<String> {
        return try {
            val uid = requireUserId()
            val trimmedCourse = if (course == "Other") {
                customCourse?.trim().orEmpty()
            } else {
                course.trim()
            }
            val dedupeKey = "${position.trim()}::$trimmedCourse"

            val collectionRef = db.collection("users").document(uid).collection("job_applications")

            // Duplicate submission guard
            val existing = collectionRef
                .whereEqualTo("dedupeKey", dedupeKey)
                .get()
                .await()

            if (!existing.isEmpty) {
                return Result.failure(
                    IllegalStateException("You have already submitted an application for $position ($trimmedCourse).")
                )
            }

            val normalizedCnic = cnicNumber.filter { it.isDigit() }
            val cnicMasked = maskCnic(normalizedCnic)
            val cnicDigest = sha256Hex("$uid::$normalizedCnic")

            val docRef = collectionRef.document()
            val payload = mutableMapOf<String, Any>(
                "userId" to uid,
                "fullName" to fullName.trim(),
                "email" to email.trim(),
                "mobileNumber" to mobileNumber.trim(),
                "cnicMasked" to cnicMasked,
                "cnicDigest" to cnicDigest,
                "position" to position.trim(),
                "course" to course.trim(),
                "dedupeKey" to dedupeKey,
                "status" to "submitted",
                "createdAt" to FieldValue.serverTimestamp(),
                "updatedAt" to FieldValue.serverTimestamp()
            )
            if (course == "Other" && trimmedCourse.isNotEmpty()) {
                payload["customCourse"] = trimmedCourse
            }

            docRef.set(payload).await()
            Result.success(docRef.id)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            val uid = auth.currentUser?.uid ?: "unauthenticated"
            handleFirestoreError(e, OperationType.CREATE, "users/$uid/job_applications")
            Result.failure(e)
        }
    }

    suspend fun upsertUserProfile(userId: String, fullName: String, email: String) {
        val docRef = db.collection("users").document(userId)
        try {
            val existing = docRef.get().await()
            if (existing.exists()) {
                docRef.update(
                    mapOf(
                        "fullName" to fullName.take(80).ifBlank { "Portfolio Visitor" },
                        "email" to email.take(120),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            } else {
                docRef.set(
                    mapOf(
                        "userId" to userId,
                        "fullName" to fullName.take(80).ifBlank { "Portfolio Visitor" },
                        "email" to email.take(120),
                        "createdAt" to FieldValue.serverTimestamp(),
                        "updatedAt" to FieldValue.serverTimestamp()
                    )
                ).await()
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, docRef.path)
        }
    }

    companion object {
        fun maskCnic(digitsOnly: String): String {
            val lastDigit = digitsOnly.lastOrNull() ?: '*'
            return "*****-*******-$lastDigit"
        }

        fun sha256Hex(input: String): String {
            val digest = MessageDigest.getInstance("SHA-256").digest(input.toByteArray(Charsets.UTF_8))
            return digest.joinToString("") { "%02x".format(it) }
        }
    }
}
