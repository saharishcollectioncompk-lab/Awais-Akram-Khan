package com.example.ui.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.JobApplication
import com.example.data.model.PortfolioCatalog
import com.example.data.model.PortfolioCategory
import com.example.data.model.PortfolioProject
import com.example.data.repository.JobApplicationRepository
import com.example.data.repository.PortfolioChatRepository
import com.example.data.repository.ThemePreferenceRepository
import com.example.util.NetworkMonitor
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JobApplicationFormState(
    val fullName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val cnicNumber: String = "",
    val selectedPosition: String = "",
    val selectedCourse: String = "",
    val customCourse: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val successMessage: String? = null
)

data class ChatMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val isError: Boolean = false
)

data class ChatUiState(
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val isGenerating: Boolean = false,
    val streamingText: String = "",
    val errorMessage: String? = null
) {
    companion object {
        const val WELCOME_MESSAGE =
            "Hey there! \uD83D\uDC4B\nI'm Awais Akram's AI assistant. Ask me anything about his experience, projects, or how to hire him."

        const val GRAPHIC_DESIGN_QUESTION = "What is Awais's graphic designing?"

        val GRAPHIC_DESIGN_ANSWER = """
            Awais Akram Khan uses Canva as his graphic designing tool to create creative, professional, and visually appealing designs. His graphic designing work includes social media posts, promotional posters, business advertisements, logos, CVs and resumes, banners, flyers, brochures, business cards, certificates, presentations, YouTube thumbnails, Facebook covers, Instagram posts, and branding materials.

            In Canva, he works with a wide range of design elements and features, including text styles and typography, fonts, shapes, lines, icons, illustrations, stickers, frames, grids, backgrounds, gradients, color combinations, images, photo editing, transparency, shadows, borders, and layout arrangements. He also uses alignment, spacing, layering, grouping, cropping, resizing, and other design tools to organize elements and create balanced compositions.

            His design process includes selecting suitable templates, customizing layouts, combining visual elements, choosing matching colors, creating attractive headlines, arranging content, and maintaining consistent branding. He can create designs for businesses, educational institutes, personal brands, and social media platforms.

            Canva helps Awais turn ideas into clear, attractive, and professional visual designs.
        """.trimIndent()

        val SUGGESTED_QUESTIONS = listOf(
            GRAPHIC_DESIGN_QUESTION,
            "Tell me about his projects.",
            "Is he available for hire?",
            "How can I contact him?"
        )
    }
}

class PortfolioViewModel(
    private val applicationRepository: JobApplicationRepository,
    private val themeRepository: ThemePreferenceRepository,
    private val chatRepository: PortfolioChatRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnlineFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = networkMonitor.isCurrentlyOnline()
        )

    val darkThemeOverride: StateFlow<Boolean?> = themeRepository.darkThemeOverride
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = null
        )

    private val _selectedCategory = MutableStateFlow(PortfolioCategory.ALL)
    val selectedCategory: StateFlow<PortfolioCategory> = _selectedCategory.asStateFlow()

    private val _selectedProject = MutableStateFlow<PortfolioProject?>(null)
    val selectedProject: StateFlow<PortfolioProject?> = _selectedProject.asStateFlow()

    private val _formState = MutableStateFlow(JobApplicationFormState())
    val formState: StateFlow<JobApplicationFormState> = _formState.asStateFlow()

    private val _submittedApplications = MutableStateFlow<List<JobApplication>>(emptyList())
    val submittedApplications: StateFlow<List<JobApplication>> = _submittedApplications.asStateFlow()

    private val _chatState = MutableStateFlow(ChatUiState())
    val chatState: StateFlow<ChatUiState> = _chatState.asStateFlow()

    private var applicationsListenerJob: Job? = null
    private var lastSubmittedKey: String? = null
    private var lastSubmittedAtMs: Long = 0L

    fun onAuthUserChanged(user: FirebaseUser?) {
        applicationsListenerJob?.cancel()
        applicationsListenerJob = null
        if (user == null) {
            _submittedApplications.value = emptyList()
            return
        }

        // Prefill name/email cleanly when user signs in (without overwriting edited values)
        _formState.update { current ->
            current.copy(
                fullName = current.fullName.ifBlank { user.displayName.orEmpty() },
                email = current.email.ifBlank { user.email.orEmpty() }
            )
        }

        viewModelScope.launch {
            applicationRepository.upsertUserProfile(
                userId = user.uid,
                fullName = user.displayName ?: "Portfolio Visitor",
                email = user.email.orEmpty()
            )
        }

        applicationsListenerJob = viewModelScope.launch {
            applicationRepository.observeApplications(user.uid)
                .catch { _ ->
                    // Handled safely without crashing Compose collector
                }
                .collect { apps ->
                    _submittedApplications.value = apps
                }
        }
    }

    fun toggleTheme(currentlyDark: Boolean) {
        viewModelScope.launch {
            themeRepository.setDarkTheme(!currentlyDark)
        }
    }

    fun selectCategory(category: PortfolioCategory) {
        _selectedCategory.value = category
    }

    fun openProject(project: PortfolioProject) {
        _selectedProject.value = project
    }

    fun closeProject() {
        _selectedProject.value = null
    }

    fun updateFullName(value: String) {
        _formState.update { it.copy(fullName = value, errorMessage = null, successMessage = null) }
    }

    fun updateEmail(value: String) {
        _formState.update { it.copy(email = value, errorMessage = null, successMessage = null) }
    }

    fun updateMobileNumber(value: String) {
        _formState.update { it.copy(mobileNumber = value, errorMessage = null, successMessage = null) }
    }

    fun updateCnicNumber(value: String) {
        _formState.update { it.copy(cnicNumber = value, errorMessage = null, successMessage = null) }
    }

    fun selectPosition(position: String) {
        _formState.update { it.copy(selectedPosition = position, errorMessage = null, successMessage = null) }
    }

    fun selectCourse(course: String) {
        _formState.update {
            it.copy(
                selectedCourse = course,
                customCourse = if (course == "Other") it.customCourse else "",
                errorMessage = null,
                successMessage = null
            )
        }
    }

    fun updateCustomCourse(value: String) {
        _formState.update { it.copy(customCourse = value, errorMessage = null, successMessage = null) }
    }

    fun submitJobApplication(isUserSignedIn: Boolean, onRequireSignIn: () -> Unit) {
        val state = _formState.value
        if (state.isSubmitting) return

        val name = state.fullName.trim()
        val email = state.email.trim()
        val mobile = state.mobileNumber.trim()
        val cnicDigits = state.cnicNumber.filter { it.isDigit() }
        val position = state.selectedPosition.trim()
        val course = state.selectedCourse.trim()
        val customCourse = state.customCourse.trim()

        val validationError = when {
            name.length < 2 -> "Please enter your full name."
            email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                "Please enter a valid Gmail / email address."
            mobile.filter { it.isDigit() }.length < 10 ->
                "Please enter a valid mobile / WhatsApp number."
            cnicDigits.length != 13 ->
                "Please enter a valid 13-digit CNIC number (e.g. 35202-1234567-1)."
            position !in PortfolioCatalog.jobPositions ->
                "Please select your job position (Teacher or Manager)."
            course !in PortfolioCatalog.jobCourses ->
                "Please select the course / subject you have learned."
            course == "Other" && customCourse.length < 2 ->
                "Please enter the course / subject name."
            else -> null
        }

        if (validationError != null) {
            _formState.update { it.copy(errorMessage = validationError, successMessage = null) }
            return
        }

        if (!isUserSignedIn) {
            _formState.update {
                it.copy(
                    errorMessage = "Please sign in with Google first to submit your application securely.",
                    successMessage = null
                )
            }
            onRequireSignIn()
            return
        }

        val resolvedCourse = if (course == "Other") customCourse else course
        val submissionKey = "$position::$resolvedCourse"
        val now = System.currentTimeMillis()
        if (lastSubmittedKey == submissionKey && (now - lastSubmittedAtMs) < 5000L) {
            _formState.update {
                it.copy(errorMessage = "Your application is already being processed.")
            }
            return
        }

        viewModelScope.launch {
            _formState.update { it.copy(isSubmitting = true, errorMessage = null, successMessage = null) }
            val result = applicationRepository.submitApplication(
                fullName = name,
                email = email,
                mobileNumber = mobile,
                cnicNumber = state.cnicNumber,
                position = position,
                course = course,
                customCourse = if (course == "Other") customCourse else null
            )
            result.fold(
                onSuccess = {
                    lastSubmittedKey = submissionKey
                    lastSubmittedAtMs = System.currentTimeMillis()
                    _formState.update {
                        it.copy(
                            cnicNumber = "",
                            selectedPosition = "",
                            selectedCourse = "",
                            customCourse = "",
                            isSubmitting = false,
                            errorMessage = null,
                            successMessage = "Application submitted successfully for $position ($resolvedCourse). Your CNIC is protected privately."
                        )
                    }
                },
                onFailure = { error ->
                    _formState.update {
                        it.copy(
                            isSubmitting = false,
                            errorMessage = error.localizedMessage ?: "Failed to submit application. Please try again.",
                            successMessage = null
                        )
                    }
                }
            )
        }
    }

    fun updateChatInput(text: String) {
        _chatState.update { it.copy(inputText = text, errorMessage = null) }
    }

    private var lastChatSentAtMs: Long = 0L

    fun sendChatMessage(overrideText: String? = null) {
        val raw = (overrideText ?: _chatState.value.inputText).trim()
        if (raw.isEmpty() || _chatState.value.isGenerating) return
        if (raw.length > 800) {
            _chatState.update { it.copy(errorMessage = "Message is too long. Please keep it under 800 characters.") }
            return
        }

        val now = System.currentTimeMillis()
        if (overrideText == null && (now - lastChatSentAtMs) < 1200L) {
            _chatState.update { it.copy(errorMessage = "Please wait a moment before sending another message.") }
            return
        }
        lastChatSentAtMs = now

        val userMsg = ChatMessage(
            id = "u_$now",
            text = raw,
            isUser = true
        )

        if (raw.equals(ChatUiState.GRAPHIC_DESIGN_QUESTION, ignoreCase = true)) {
            chatRepository.seedTurn(raw, ChatUiState.GRAPHIC_DESIGN_ANSWER)
            val botMsg = ChatMessage(
                id = "m_${now + 1}",
                text = ChatUiState.GRAPHIC_DESIGN_ANSWER,
                isUser = false
            )
            _chatState.update {
                it.copy(
                    messages = it.messages + userMsg + botMsg,
                    inputText = if (overrideText == null) "" else it.inputText,
                    isGenerating = false,
                    streamingText = "",
                    errorMessage = null
                )
            }
            return
        }

        _chatState.update {
            it.copy(
                messages = it.messages + userMsg,
                inputText = if (overrideText == null) "" else it.inputText,
                isGenerating = true,
                streamingText = "",
                errorMessage = null
            )
        }

        viewModelScope.launch {
            var latestReply = ""
            try {
                chatRepository.sendMessageStream(raw).collect { accumulated ->
                    latestReply = accumulated
                    _chatState.update { it.copy(streamingText = accumulated) }
                }
                val botMsg = ChatMessage(
                    id = "m_${System.currentTimeMillis()}",
                    text = latestReply,
                    isUser = false
                )
                _chatState.update {
                    it.copy(
                        messages = it.messages + botMsg,
                        isGenerating = false,
                        streamingText = ""
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                val errText = e.localizedMessage ?: "Unable to connect to the AI assistant right now. Please try again."
                _chatState.update {
                    it.copy(
                        isGenerating = false,
                        streamingText = "",
                        errorMessage = null,
                        messages = it.messages + ChatMessage(
                            id = "e_${System.currentTimeMillis()}",
                            text = errText,
                            isUser = false,
                            isError = true
                        )
                    )
                }
            }
        }
    }

    fun startNewChat() {
        chatRepository.resetChat()
        _chatState.value = ChatUiState()
    }
}
