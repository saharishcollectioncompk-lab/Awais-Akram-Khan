package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.auth.attemptAutoSignIn
import com.example.auth.authStateFlow
import com.example.data.repository.JobApplicationRepository
import com.example.data.repository.PortfolioChatRepository
import com.example.data.repository.ThemePreferenceRepository
import com.example.firebase.configureAppCheck
import com.example.ui.components.AboutAndJourneySections
import com.example.ui.components.AskMeChatDialog
import com.example.ui.components.AuthDialog
import com.example.ui.components.ContactInformationSection
import com.example.ui.components.FloatingRightActionStack
import com.example.ui.components.HeroSection
import com.example.ui.components.JobApplicationSection
import com.example.ui.components.OfflineGateScreen
import com.example.ui.components.PortfolioFooter
import com.example.ui.components.PortfolioSection
import com.example.ui.components.PortfolioShowcaseSection
import com.example.ui.components.PortfolioTopHeader
import com.example.ui.components.ProjectMockupDetailDialog
import com.example.ui.theme.AwaisPortfolioTheme
import com.example.ui.viewmodel.PortfolioViewModel
import com.example.util.NetworkMonitor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Step 1: Configure App Check with launch intent before rendering Compose UI or creating ViewModels
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            PortfolioAppRoot()
        }
    }
}

@Composable
fun PortfolioAppRoot() {
    val context = LocalContext.current
    val viewModel: PortfolioViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val app = checkNotNull(this[APPLICATION_KEY])
                val databaseId = app.getString(R.string.firestore_database_id)
                val firestore = FirebaseFirestore.getInstance(databaseId)
                PortfolioViewModel(
                    applicationRepository = JobApplicationRepository(firestore),
                    themeRepository = ThemePreferenceRepository(app),
                    chatRepository = PortfolioChatRepository(),
                    networkMonitor = NetworkMonitor(app)
                )
            }
        }
    )

    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val darkThemeOverride by viewModel.darkThemeOverride.collectAsStateWithLifecycle()
    val systemDark = isSystemInDarkTheme()
    val effectiveDark = darkThemeOverride ?: systemDark

    val currentUser by Firebase.auth.authStateFlow()
        .collectAsStateWithLifecycle(initialValue = Firebase.auth.currentUser)

    LaunchedEffect(currentUser?.uid) {
        viewModel.onAuthUserChanged(currentUser)
    }

    // Attempt silent Google auto-sign-in once when online and unauthenticated
    val credentialManager = remember(context) { CredentialManager.create(context) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(isOnline) {
        if (isOnline && Firebase.auth.currentUser == null) {
            attemptAutoSignIn(
                context = context,
                credentialManager = credentialManager,
                scope = coroutineScope,
                onAuthSuccess = {},
                onUnauthenticated = {}
            )
        }
        onDispose {}
    }

    AwaisPortfolioTheme(darkThemeOverride = darkThemeOverride) {
        if (!isOnline) {
            OfflineGateScreen(
                onRetry = {
                    // Re-evaluates network state; NetworkMonitor flow automatically unlocks when online
                }
            )
        } else {
            PortfolioScreen(
                viewModel = viewModel,
                currentUser = currentUser,
                isDark = effectiveDark,
                onToggleTheme = { viewModel.toggleTheme(effectiveDark) }
            )
        }
    }
}

@Composable
fun PortfolioScreen(
    viewModel: PortfolioViewModel,
    currentUser: com.google.firebase.auth.FirebaseUser?,
    isDark: Boolean,
    onToggleTheme: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedProject by viewModel.selectedProject.collectAsStateWithLifecycle()
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val submittedApplications by viewModel.submittedApplications.collectAsStateWithLifecycle()
    val chatState by viewModel.chatState.collectAsStateWithLifecycle()

    var showAuthDialog by remember { mutableStateOf(false) }
    var showAskMeChat by remember { mutableStateOf(false) }

    val activeSection by remember {
        derivedStateOf {
            when (listState.firstVisibleItemIndex) {
                0 -> PortfolioSection.HOME
                1 -> PortfolioSection.ABOUT
                2 -> PortfolioSection.PORTFOLIO
                3 -> PortfolioSection.APPLY
                else -> PortfolioSection.CONTACT
            }
        }
    }

    fun scrollToSection(section: PortfolioSection) {
        val targetIndex = when (section) {
            PortfolioSection.HOME -> 0
            PortfolioSection.ABOUT -> 1
            PortfolioSection.PORTFOLIO -> 2
            PortfolioSection.APPLY -> 3
            PortfolioSection.CONTACT -> 4
        }
        scope.launch {
            listState.animateScrollToItem(targetIndex)
        }
    }

    // BackHandler closes open modals cleanly before exiting
    BackHandler(enabled = selectedProject != null || showAskMeChat || showAuthDialog) {
        when {
            selectedProject != null -> viewModel.closeProject()
            showAskMeChat -> showAskMeChat = false
            showAuthDialog -> showAuthDialog = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                PortfolioTopHeader(
                    isDark = isDark,
                    currentUser = currentUser,
                    activeSection = activeSection,
                    onToggleTheme = onToggleTheme,
                    onOpenAuth = { showAuthDialog = true },
                    onNavigateSection = { scrollToSection(it) }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // 0: Hero / Welcome Section
                item(key = "hero") {
                    HeroSection(
                        onExplorePortfolio = { scrollToSection(PortfolioSection.PORTFOLIO) },
                        onApplyTeam = { scrollToSection(PortfolioSection.APPLY) }
                    )
                }

                // 1: About Me, Digital Teaching Journey & Profile Card
                item(key = "about") {
                    AboutAndJourneySections()
                }

                // 2: My Portfolio Showcase (26 real works in 2D mockups)
                item(key = "portfolio") {
                    PortfolioShowcaseSection(
                        selectedCategory = selectedCategory,
                        onSelectCategory = viewModel::selectCategory,
                        onProjectClick = viewModel::openProject
                    )
                }

                // 3: Job Application – Awais Akram Khan (Placed ABOVE Contact Information)
                item(key = "apply") {
                    JobApplicationSection(
                        formState = formState,
                        submittedApplications = submittedApplications,
                        isUserSignedIn = currentUser != null,
                        onFullNameChange = viewModel::updateFullName,
                        onEmailChange = viewModel::updateEmail,
                        onMobileChange = viewModel::updateMobileNumber,
                        onCnicChange = viewModel::updateCnicNumber,
                        onSelectPosition = viewModel::selectPosition,
                        onSelectCourse = viewModel::selectCourse,
                        onCustomCourseChange = viewModel::updateCustomCourse,
                        onSubmit = {
                            viewModel.submitJobApplication(
                                isUserSignedIn = currentUser != null,
                                onRequireSignIn = { showAuthDialog = true }
                            )
                        },
                        onOpenSignIn = { showAuthDialog = true }
                    )
                }

                // 4: Contact Information (Strictly BELOW Job Application)
                item(key = "contact") {
                    ContactInformationSection()
                }

                // 5: Footer with right-aligned Social Media Icons
                item(key = "footer") {
                    PortfolioFooter()
                }
            }
        }

        FloatingRightActionStack(
            onOpenAskMe = { showAskMeChat = true },
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }

    selectedProject?.let { project ->
        ProjectMockupDetailDialog(
            project = project,
            onDismiss = viewModel::closeProject
        )
    }

    if (showAuthDialog) {
        AuthDialog(
            currentUser = currentUser,
            onDismiss = { showAuthDialog = false }
        )
    }

    if (showAskMeChat) {
        AskMeChatDialog(
            chatState = chatState,
            onInputChange = viewModel::updateChatInput,
            onSend = { overrideText -> viewModel.sendChatMessage(overrideText) },
            onNewChat = viewModel::startNewChat,
            onDismiss = { showAskMeChat = false }
        )
    }
}
