package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.UUID

class JobApplicationRepositoryRuleTest : FirestoreEmulatorTestBase() {

    @Test
    fun submitApplication_authenticatedUser_persistsApplicationWithMaskedCnic() = runBlocking {
        val uid = signInTestUser("applicant_${UUID.randomUUID()}@gmail.com")
        val repository = JobApplicationRepository(firestore, auth)

        val result = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.submitApplication(
                fullName = "Ayesha Siddiqui",
                email = "ayesha@gmail.com",
                mobileNumber = "03001234567",
                cnicNumber = "35202-1234567-3",
                position = "Teacher",
                course = "Graphic Designing",
                customCourse = null
            )
        }

        assertTrue("Expected success but got ${result.exceptionOrNull()}", result.isSuccess)
        val apps = withTimeout(FLOW_TIMEOUT_MS) {
            repository.observeApplications(uid).first { it.isNotEmpty() }
        }
        assertEquals(1, apps.size)
        assertEquals("*****-*******-3", apps.first().cnicMasked)
        assertEquals("Teacher", apps.first().position)
    }

    @Test
    fun submitApplication_duplicatePositionAndCourse_isRejected() = runBlocking {
        signInTestUser("dup_${UUID.randomUUID()}@gmail.com")
        val repository = JobApplicationRepository(firestore, auth)

        val first = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.submitApplication(
                fullName = "Bilal Ahmed",
                email = "bilal@gmail.com",
                mobileNumber = "03211234567",
                cnicNumber = "35201-9876543-1",
                position = "Manager",
                course = "Microsoft Office",
                customCourse = null
            )
        }
        assertTrue(first.isSuccess)

        val duplicate = withTimeout(DEFAULT_TIMEOUT_MS) {
            repository.submitApplication(
                fullName = "Bilal Ahmed",
                email = "bilal@gmail.com",
                mobileNumber = "03211234567",
                cnicNumber = "35201-9876543-1",
                position = "Manager",
                course = "Microsoft Office",
                customCourse = null
            )
        }
        assertTrue(duplicate.isFailure)
    }

    @Test
    fun observeApplications_unauthenticatedCaller_failsWithPermissionDenied() = runBlocking {
        auth.signOut()
        val repository = JobApplicationRepository(firestore, auth)

        var caught: Exception? = null
        try {
            withTimeout(FLOW_TIMEOUT_MS) {
                repository.observeApplications("some_other_uid").first()
            }
        } catch (e: Exception) {
            caught = e
        }

        val firestoreException = checkNotNull(caught?.unwrapFirestoreException()) {
            "Expected nested FirebaseFirestoreException, got $caught"
        }
        assertEquals(FirebaseFirestoreException.Code.PERMISSION_DENIED, firestoreException.code)
    }

    private companion object {
        const val DEFAULT_TIMEOUT_MS = 5000L
        const val FLOW_TIMEOUT_MS = 4000L
    }
}
