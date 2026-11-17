package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.ECZGradeLevel
import com.example.data.models.ECZPastPaper
import com.example.data.models.ECZSubjectsData
import com.example.data.models.PastPaperQuestion
import com.example.ui.theme.*
import com.example.ui.viewmodels.ECZViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastPapersScreen(
    viewModel: ECZViewModel,
    onNavigateToMockExams: () -> Unit = {},
    onSelectQuestionDetail: (PastPaperQuestion) -> Unit = {},
    onSelectSubject: (String) -> Unit = {},
    onSelectPaper: (ECZPastPaper) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedGrade by viewModel.selectedGrade.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val subjectFilter by viewModel.pastPaperSubjectFilter.collectAsStateWithLifecycle()
    val revealedAnswers by viewModel.revealedAnswers.collectAsStateWithLifecycle()
    val downloadedPaperIds by viewModel.downloadedPaperIds.collectAsStateWithLifecycle()
    val filterOnlyDownloaded by viewModel.filterOnlyDownloaded.collectAsStateWithLifecycle()
    val downloadingPaperId by viewModel.downloadingPaperId.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: Browse, 1: Mock exams

    val availablePaperDocs = viewModel.getPastPaperDocuments()
    val realQuestions = viewModel.getFilteredPastPapers()

    Scaffold(
        topBar = {
            Surface(
                color = DarkBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = TagGreenBg,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Text(
                            text = "Past Papers",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            ),
                            color = TextWhite
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tab selector (Browse vs Mock exams)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .clickable { selectedTab = 0 }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Browse",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (selectedTab == 0) TextWhite else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (selectedTab == 0) {
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(2.5.dp)
                                        .background(NeonGreen, RoundedCornerShape(2.dp))
                                )
                            }
                        }

                        Column(
                            modifier = Modifier
                                .clickable {
                                    selectedTab = 1
                                    onNavigateToMockExams()
                                }
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = "Mock exams",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (selectedTab == 1) TextWhite else TextSecondary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (selectedTab == 1) {
                                Box(
                                    modifier = Modifier
                                        .width(36.dp)
                                        .height(2.5.dp)
                                        .background(NeonGreen, RoundedCornerShape(2.dp))
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = DarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Search Input Box (Matches Screen 3 & 4)
            item {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = DarkSurfaceVariant,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Box(modifier = Modifier.weight(1f)) {
                            if (searchQuery.isEmpty()) {
                                Text(
                                    text = "Search by topic (e.g. quadratic equations)",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextMuted
                                )
                            }
                            androidx.compose.foundation.text.BasicTextField(
                                value = searchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                textStyle = MaterialTheme.typography.bodyMedium.copy(color = TextWhite),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Filter Chips: All Subjects + Offline + Available Subjects for Selected Grade
            item {
                val availableSubjects = remember(selectedGrade) {
                    ECZSubjectsData.getSubjectsForGrade(selectedGrade)
                }
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChipButton(
                            text = "All Subjects",
                            isSelected = !filterOnlyDownloaded && (subjectFilter == "All Subjects" || subjectFilter.isBlank()),
                            onClick = {
                                if (filterOnlyDownloaded) viewModel.toggleFilterOnlyDownloaded()
                                viewModel.setPastPaperSubject("All Subjects")
                            }
                        )
                    }
                    item {
                        FilterChipButton(
                            text = if (downloadedPaperIds.isNotEmpty()) "Offline (${downloadedPaperIds.size})" else "Offline",
                            isSelected = filterOnlyDownloaded,
                            onClick = { viewModel.toggleFilterOnlyDownloaded() }
                        )
                    }
                    items(availableSubjects) { subject ->
                        FilterChipButton(
                            text = subject,
                            isSelected = !filterOnlyDownloaded && subjectFilter == subject,
                            onClick = {
                                if (filterOnlyDownloaded) viewModel.toggleFilterOnlyDownloaded()
                                if (subjectFilter == subject) {
                                    onSelectSubject(subject)
                                } else {
                                    viewModel.setPastPaperSubject(subject)
                                }
                            }
                        )
                    }
                }
            }

            // Section 1: Official ECZ Past Paper Documents
            if (availablePaperDocs.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (filterOnlyDownloaded) "Downloaded Papers (Offline)" else "Official ECZ Papers (${availablePaperDocs.size})",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = TextWhite
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = TagGreenBg
                        ) {
                            Text(
                                text = "${selectedGrade.displayName}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = NeonGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                items(availablePaperDocs) { paper ->
                    val isPaperDownloaded = downloadedPaperIds.contains(paper.id)
                    val isPaperDownloading = downloadingPaperId == paper.id

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isPaperDownloaded) NeonGreen.copy(alpha = 0.4f) else DarkCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("paper_doc_card_${paper.id}")
                            .clickable { onSelectPaper(paper) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = TagGreenBg,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                                    ) {
                                        Text(
                                            text = "ECZ ${paper.year}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = NeonGreen,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = DarkSurfaceVariant
                                    ) {
                                        Text(
                                            text = paper.paperCode,
                                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                                            color = TextSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                if (isPaperDownloaded) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = TagGreenBg
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        ) {
                                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(12.dp))
                                            Text(
                                                text = "Offline Ready",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                                color = NeonGreen
                                            )
                                        }
                                    }
                                } else {
                                    Text(
                                        text = paper.fileSize,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextSecondary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = paper.title,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextWhite
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "${paper.questions.size} Questions with full marking scheme • ${paper.duration} • ${paper.totalMarks} marks",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = { onSelectPaper(paper) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = NeonGreen,
                                        contentColor = Color.Black
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("view_paper_btn_${paper.id}")
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Text("View Paper", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                                    }
                                }

                                if (isPaperDownloading) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .background(DarkSurfaceVariant, RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator(strokeWidth = 2.dp, color = NeonGreen, modifier = Modifier.size(20.dp))
                                    }
                                } else if (isPaperDownloaded) {
                                    OutlinedButton(
                                        onClick = { viewModel.deleteDownloadedPaper(paper.id) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("delete_offline_btn_${paper.id}")
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Offline", modifier = Modifier.size(16.dp))
                                    }
                                } else {
                                    OutlinedButton(
                                        onClick = { viewModel.downloadPaper(paper) },
                                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonGreen),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.testTag("download_offline_btn_${paper.id}")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(Icons.Default.FileDownload, contentDescription = null, modifier = Modifier.size(16.dp))
                                            Text("Download", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }

            // Section 2: Questions & Marking Scheme
            item {
                Text(
                    text = "Topic Questions & Marking Scheme",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp
                    ),
                    color = TextWhite
                )
            }

            if (realQuestions.isEmpty() && availablePaperDocs.isEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No past papers available yet",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "All materials are empty until uploaded. Scan or upload an exam paper to begin!",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            } else {
                items(realQuestions) { question ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("past_paper_q_${question.id}")
                            .clickable { onSelectQuestionDetail(question) }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = TagGreenBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = question.questionNumber,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NeonGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }

                                Text(
                                    text = "${question.marks} marks",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = question.questionText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TextWhite
                            )
                            Spacer(modifier = Modifier.height(6.dp))

                            val isAnswerRevealed = revealedAnswers[question.id] ?: false
                            if (isAnswerRevealed) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = DarkSurfaceVariant,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "Official Answer: ${question.markingSchemeAnswer}",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = NeonGreen
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = question.stepByStepExplanation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextSecondary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Topic: ${question.topic}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextMuted
                                )

                                TextButton(
                                    onClick = { viewModel.toggleAnswerRevealed(question.id) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = if (isAnswerRevealed) "Hide Answer" else "Reveal Answer",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NeonGreen
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Mock Exams Card (Matches Screen 3)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToMockExams)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = TagGreenBg,
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Assignment,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Column {
                                Text(
                                    text = "Full mock exams",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextWhite
                                )
                                Text(
                                    text = "Timed practice • All subjects",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Get Unlimited Access Card (Matches Screen 3)
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.WorkspacePremium,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(22.dp)
                            )
                            Column {
                                Text(
                                    text = "Get unlimited access",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextWhite
                                )
                                Text(
                                    text = "Remove limits and ads",
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                    color = TextSecondary
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = NeonGreen,
                            modifier = Modifier.clickable { }
                        ) {
                            Text(
                                text = "Go unlimited",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 11.sp
                                ),
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }

            // Save for offline Switch (Matches Screen 4)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            tint = if (filterOnlyDownloaded) NeonGreen else TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                        Column {
                            Text(
                                text = "Save for offline / Show downloaded",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = TextWhite
                            )
                            Text(
                                text = "${downloadedPaperIds.size} paper(s) downloaded to device",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSecondary
                            )
                        }
                    }

                    Switch(
                        checked = filterOnlyDownloaded,
                        onCheckedChange = { viewModel.toggleFilterOnlyDownloaded() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.Black,
                            checkedTrackColor = NeonGreen,
                            uncheckedTrackColor = DarkSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterChipButton(
    text: String,
    isSelected: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) TagGreenBg else DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) NeonGreen else DarkCardBorder
        ),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = if (isSelected) NeonGreen else TextWhite
            )
        }
    }
}
