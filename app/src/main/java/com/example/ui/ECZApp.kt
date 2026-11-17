package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.models.ECZPastPaper
import com.example.data.models.PastPaperQuestion
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodels.ECZViewModel

enum class ECZNavTab(val title: String) {
    HOME("Home"),
    AI_TUTOR("AI Tutor"),
    SCANNER("AI Scan"),
    PAST_PAPERS("Past Papers"),
    REVISION_HUB("Revision")
}

@Composable
fun ECZApp(
    viewModel: ECZViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    var currentTab by remember { mutableStateOf(ECZNavTab.HOME) }
    var showMockExams by remember { mutableStateOf(false) }
    var showChooseSubjects by remember { mutableStateOf(false) }
    var selectedSubjectDetail by remember { mutableStateOf<String?>(null) }
    var selectedPaperForViewing by remember { mutableStateOf<ECZPastPaper?>(null) }
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()

    // Handle back button for any sub-screen or non-home tab
    BackHandler(enabled = selectedPaperForViewing != null || selectedSubjectDetail != null || showChooseSubjects || showMockExams || currentTab != ECZNavTab.HOME) {
        when {
            selectedPaperForViewing != null -> selectedPaperForViewing = null
            selectedSubjectDetail != null -> selectedSubjectDetail = null
            showChooseSubjects -> showChooseSubjects = false
            showMockExams -> showMockExams = false
            else -> currentTab = ECZNavTab.HOME
        }
    }

    Scaffold(
        bottomBar = {
            if (selectedPaperForViewing == null && !showMockExams && !showChooseSubjects && selectedSubjectDetail == null) {
                Surface(
                    color = DarkBackground,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("main_navigation_bar")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp, vertical = 6.dp)
                            .navigationBarsPadding(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tab 1: Home
                        NavTabItem(
                            title = "Home",
                            icon = if (currentTab == ECZNavTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                            isSelected = currentTab == ECZNavTab.HOME,
                            testTag = "nav_tab_home",
                            onClick = {
                                showMockExams = false
                                showChooseSubjects = false
                                selectedSubjectDetail = null
                                currentTab = ECZNavTab.HOME
                            }
                        )

                        // Tab 2: AI Tutor
                        NavTabItem(
                            title = "AI Tutor",
                            icon = if (currentTab == ECZNavTab.AI_TUTOR) Icons.Filled.SmartToy else Icons.Outlined.SmartToy,
                            isSelected = currentTab == ECZNavTab.AI_TUTOR,
                            testTag = "nav_tab_ai_tutor",
                            onClick = {
                                showMockExams = false
                                showChooseSubjects = false
                                selectedSubjectDetail = null
                                currentTab = ECZNavTab.AI_TUTOR
                            }
                        )

                        // Tab 3: CENTERPIECE BIG ROUND AI SCANNER BUTTON (Matches All 8 Screens in Mockup)
                        Box(
                            modifier = Modifier
                                .offset(y = (-18).dp)
                                .testTag("big_center_ai_scanner_button")
                                .clickable {
                                    showMockExams = false
                                    showChooseSubjects = false
                                    selectedSubjectDetail = null
                                    currentTab = ECZNavTab.SCANNER
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    shadowElevation = 12.dp,
                                    color = DarkBackground,
                                    modifier = Modifier
                                        .size(72.dp)
                                        .border(
                                            width = 3.dp,
                                            color = if (currentTab == ECZNavTab.SCANNER) NeonGreen else DarkCardBorder,
                                            shape = CircleShape
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(DarkSurface),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(58.dp)
                                                .border(
                                                    width = 1.dp,
                                                    color = if (currentTab == ECZNavTab.SCANNER) NeonGreen.copy(alpha = 0.5f) else DarkCardBorder,
                                                    shape = CircleShape
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.DocumentScanner,
                                                contentDescription = "AI Scanner",
                                                tint = if (currentTab == ECZNavTab.SCANNER) NeonGreen else TextWhite,
                                                modifier = Modifier.size(34.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = if (currentTab == ECZNavTab.SCANNER) TagGreenBg else DarkSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (currentTab == ECZNavTab.SCANNER) NeonGreen else DarkCardBorder),
                                    modifier = Modifier.padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = "AI Scan",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 10.sp
                                        ),
                                        color = if (currentTab == ECZNavTab.SCANNER) NeonGreen else TextSecondary,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        // Tab 4: Past Papers
                        NavTabItem(
                            title = "Past Papers",
                            icon = if (currentTab == ECZNavTab.PAST_PAPERS) Icons.Filled.Description else Icons.Outlined.Description,
                            isSelected = currentTab == ECZNavTab.PAST_PAPERS,
                            testTag = "nav_tab_past_papers",
                            onClick = {
                                showMockExams = false
                                showChooseSubjects = false
                                selectedSubjectDetail = null
                                currentTab = ECZNavTab.PAST_PAPERS
                            }
                        )

                        // Tab 5: Revision
                        NavTabItem(
                            title = "Revision",
                            icon = if (currentTab == ECZNavTab.REVISION_HUB) Icons.Default.TrackChanges else Icons.Outlined.TrackChanges,
                            isSelected = currentTab == ECZNavTab.REVISION_HUB,
                            testTag = "nav_tab_revision_hub",
                            onClick = {
                                showMockExams = false
                                showChooseSubjects = false
                                selectedSubjectDetail = null
                                currentTab = ECZNavTab.REVISION_HUB
                            }
                        )
                    }
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = DarkBackground,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                selectedPaperForViewing != null -> {
                    PastPaperViewerScreen(
                        paper = selectedPaperForViewing!!,
                        viewModel = viewModel,
                        onNavigateBack = { selectedPaperForViewing = null },
                        onAskTutor = { question ->
                            selectedPaperForViewing = null
                            viewModel.setChatSubject(question.subject)
                            viewModel.sendChatMessage("Can you explain step-by-step how to solve this ECZ ${question.subject} ${question.year} question:\n\n${question.questionText}")
                            currentTab = ECZNavTab.AI_TUTOR
                        },
                        onStartMock = { subject ->
                            selectedPaperForViewing = null
                            viewModel.startMockExam(subject)
                            showMockExams = true
                        }
                    )
                }
                selectedSubjectDetail != null -> {
                    SubjectDetailScreen(
                        subjectName = selectedSubjectDetail!!,
                        gradeLevel = selectedGrade,
                        viewModel = viewModel,
                        onNavigateBack = { selectedSubjectDetail = null },
                        onNavigateToScanner = { subject ->
                            selectedSubjectDetail = null
                            viewModel.setChatSubject(subject)
                            currentTab = ECZNavTab.SCANNER
                        },
                        onNavigateToAITutor = { subject ->
                            selectedSubjectDetail = null
                            viewModel.setChatSubject(subject)
                            currentTab = ECZNavTab.AI_TUTOR
                        },
                        onSelectPaper = { paper ->
                            selectedPaperForViewing = paper
                        }
                    )
                }
                showChooseSubjects -> {
                    ChooseSubjectsScreen(
                        viewModel = viewModel,
                        onFinish = { showChooseSubjects = false },
                        onSubjectClick = { subject, _ ->
                            selectedSubjectDetail = subject
                        }
                    )
                }
                showMockExams -> {
                    MockExamScreen(
                        viewModel = viewModel,
                        onNavigateBack = { showMockExams = false }
                    )
                }
                else -> {
                    when (currentTab) {
                        ECZNavTab.HOME -> {
                            HomeScreen(
                                viewModel = viewModel,
                                onNavigateToScanner = { currentTab = ECZNavTab.SCANNER },
                                onNavigateToPapers = { currentTab = ECZNavTab.PAST_PAPERS },
                                onNavigateToAITutor = { currentTab = ECZNavTab.AI_TUTOR },
                                onNavigateToRevision = { currentTab = ECZNavTab.REVISION_HUB },
                                onSelectQuestionDetail = { question ->
                                    val paper = viewModel.getPastPaperDocuments().find { it.questions.any { q -> q.id == question.id } }
                                    if (paper != null) {
                                        selectedPaperForViewing = paper
                                    } else {
                                        currentTab = ECZNavTab.PAST_PAPERS
                                    }
                                },
                                onNavigateToChooseSubjects = { showChooseSubjects = true },
                                onSelectSubject = { subject -> selectedSubjectDetail = subject }
                            )
                        }
                        ECZNavTab.AI_TUTOR -> {
                            AITutorScreen(
                                viewModel = viewModel
                            )
                        }
                        ECZNavTab.SCANNER -> {
                            AIScannerScreen(
                                viewModel = viewModel,
                                onNavigateToChatWithPrompt = { prompt, subject ->
                                    viewModel.setChatSubject(subject)
                                    viewModel.sendChatMessage(prompt)
                                    currentTab = ECZNavTab.AI_TUTOR
                                },
                                onNavigateBack = { currentTab = ECZNavTab.HOME }
                            )
                        }
                        ECZNavTab.PAST_PAPERS -> {
                            PastPapersScreen(
                                viewModel = viewModel,
                                onNavigateToMockExams = { showMockExams = true },
                                onSelectQuestionDetail = { question ->
                                    val paper = viewModel.getPastPaperDocuments().find { it.questions.any { q -> q.id == question.id } }
                                    if (paper != null) {
                                        selectedPaperForViewing = paper
                                    } else {
                                        currentTab = ECZNavTab.SCANNER
                                    }
                                },
                                onSelectSubject = { subject -> selectedSubjectDetail = subject },
                                onSelectPaper = { paper -> selectedPaperForViewing = paper }
                            )
                        }
                        ECZNavTab.REVISION_HUB -> {
                            RevisionHubScreen(
                                viewModel = viewModel,
                                onNavigateToTopicPractice = { _ ->
                                    currentTab = ECZNavTab.PAST_PAPERS
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NavTabItem(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .minimumInteractiveComponentSize()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 4.dp)
            .testTag(testTag)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isSelected) NeonGreen else TextSecondary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                fontSize = 10.5.sp
            ),
            color = if (isSelected) NeonGreen else TextSecondary
        )
    }
}
