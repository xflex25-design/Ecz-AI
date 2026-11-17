package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.models.ECZPastPaper
import com.example.data.models.PastPaperQuestion
import com.example.ui.theme.*
import com.example.ui.viewmodels.ECZViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PastPaperViewerScreen(
    paper: ECZPastPaper,
    viewModel: ECZViewModel,
    onNavigateBack: () -> Unit,
    onAskTutor: (PastPaperQuestion) -> Unit,
    onStartMock: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    BackHandler {
        onNavigateBack()
    }

    val downloadedPaperIds by viewModel.downloadedPaperIds.collectAsStateWithLifecycle()
    val downloadingPaperId by viewModel.downloadingPaperId.collectAsStateWithLifecycle()
    val isDownloaded = downloadedPaperIds.contains(paper.id)
    val isDownloading = downloadingPaperId == paper.id

    // View Mode: 0: Practice (Hide answers by default), 1: Solutions & Marking Scheme (Always show)
    var viewMode by remember { mutableIntStateOf(0) }
    var revealedQuestions by remember { mutableStateOf<Set<String>>(emptySet()) }

    var showDownloadSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Surface(
                color = DarkBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            IconButton(
                                onClick = onNavigateBack,
                                modifier = Modifier.testTag("past_paper_viewer_back")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                    tint = TextWhite
                                )
                            }

                            Column {
                                Text(
                                    text = paper.title,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp
                                    ),
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${paper.gradeLevel.displayName} • ${paper.paperCode} • ${paper.duration}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = TextSecondary
                                )
                            }
                        }

                        // Offline Download Action Button
                        if (isDownloading) {
                            CircularProgressIndicator(
                                strokeWidth = 2.dp,
                                color = NeonGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (isDownloaded) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = TagGreenBg,
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .testTag("downloaded_badge_header")
                                    .clickable {
                                        viewModel.deleteDownloadedPaper(paper.id)
                                        snackbarMessage = "Removed ${paper.title} from offline downloads"
                                        showDownloadSnackbar = true
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Offline ✓",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = NeonGreen
                                    )
                                }
                            }
                        } else {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = DarkSurfaceVariant,
                                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                                modifier = Modifier
                                    .testTag("download_offline_button_header")
                                    .clickable {
                                        viewModel.downloadPaper(paper)
                                        snackbarMessage = "Downloaded ${paper.title} for offline access!"
                                        showDownloadSnackbar = true
                                    }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FileDownload,
                                        contentDescription = null,
                                        tint = NeonGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Download",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextWhite
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Mode Selector: Practice vs Solutions
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkSurfaceVariant)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = if (viewMode == 0) TagGreenBg else Color.Transparent,
                            border = if (viewMode == 0) androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewMode = 0 }
                        ) {
                            Text(
                                text = "Exam Practice Mode",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (viewMode == 0) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                ),
                                color = if (viewMode == 0) NeonGreen else TextSecondary,
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = if (viewMode == 1) TagGreenBg else Color.Transparent,
                            border = if (viewMode == 1) androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)) else null,
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewMode = 1 }
                        ) {
                            Text(
                                text = "Marking Scheme & Solutions",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (viewMode == 1) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 12.sp
                                ),
                                color = if (viewMode == 1) NeonGreen else TextSecondary,
                                modifier = Modifier
                                    .padding(vertical = 7.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkSurface,
                border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = if (isDownloaded) "Saved to device storage" else "Available for offline use",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isDownloaded) NeonGreen else TextSecondary
                        )
                        Text(
                            text = "${paper.questions.size} Questions • ${paper.totalMarks} Marks • ${paper.fileSize}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            color = TextWhite
                        )
                    }

                    if (!isDownloaded) {
                        Button(
                            onClick = {
                                viewModel.downloadPaper(paper)
                                snackbarMessage = "Downloaded ${paper.title} for offline access!"
                                showDownloadSnackbar = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                contentColor = Color.Black
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("download_paper_bottom_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                                Text("Save Offline", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                            }
                        }
                    } else {
                        OutlinedButton(
                            onClick = {
                                viewModel.deleteDownloadedPaper(paper.id)
                                snackbarMessage = "Removed ${paper.title} from offline downloads"
                                showDownloadSnackbar = true
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextSecondary),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DarkCardBorder),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.DeleteOutline, contentDescription = null, modifier = Modifier.size(14.dp))
                                Text("Remove Offline", style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        },
        containerColor = DarkBackground,
        modifier = modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(top = 12.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Official ECZ Header Card
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
                                .padding(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = TagGreenBg,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                                ) {
                                    Text(
                                        text = "EXAMINATIONS COUNCIL OF ZAMBIA",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black, fontSize = 9.sp),
                                        color = NeonGreen,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = DarkSurfaceVariant
                                ) {
                                    Text(
                                        text = paper.paperCode,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Examination for School Certificate Ordinary Level",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextWhite
                            )

                            Text(
                                text = "${paper.subject} • ${paper.paper} (${paper.year})",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = NeonGreen
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Time: ${paper.duration}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextWhite
                                )
                                Text(
                                    text = "Marks: ${paper.totalMarks}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextWhite
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF1E241E),
                                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E3D2E)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "Official Instructions to Candidates:",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = WarningAmber
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = paper.instructions,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp, lineHeight = 16.sp),
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // Section Title
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Examination Questions (${paper.questions.size})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 15.sp),
                            color = TextWhite
                        )

                        Text(
                            text = if (viewMode == 0) "Tap question to reveal answer" else "Official marking scheme view",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (viewMode == 0) NeonGreen else TextSecondary
                        )
                    }
                }

                // Questions
                itemsIndexed(paper.questions) { index, question ->
                    val isRevealed = viewMode == 1 || revealedQuestions.contains(question.id)

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurface),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isRevealed && viewMode == 1) NeonGreen.copy(alpha = 0.3f) else DarkCardBorder
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("viewer_question_${question.id}")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            // Question Header
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
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
                                    ) {
                                        Text(
                                            text = question.questionNumber,
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
                                            text = question.topic,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                            color = TextSecondary,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = DarkSurfaceVariant
                                ) {
                                    Text(
                                        text = "${question.marks} marks",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = TextWhite,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Question Statement
                            Text(
                                text = question.questionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 22.sp,
                                    fontSize = 14.5.sp
                                ),
                                color = TextWhite
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            // Practice mode: Reveal / Hide toggle button
                            if (viewMode == 0) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    TextButton(
                                        onClick = {
                                            revealedQuestions = if (revealedQuestions.contains(question.id)) {
                                                revealedQuestions - question.id
                                            } else {
                                                revealedQuestions + question.id
                                            }
                                        },
                                        colors = ButtonDefaults.textButtonColors(contentColor = NeonGreen)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (isRevealed) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = if (isRevealed) "Hide Marking Scheme" else "Check Solution & Marking Scheme",
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { onAskTutor(question) },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.Chat,
                                            contentDescription = "Ask Tutor",
                                            tint = NeonGreen,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }

                            // Marking Scheme & Step-by-Step Breakdown
                            AnimatedVisibility(visible = isRevealed) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 10.dp)
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = DarkSurfaceVariant,
                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.35f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(14.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Text(
                                                    text = "OFFICIAL ECZ ANSWER:",
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Black),
                                                    color = NeonGreen
                                                )
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = TagGreenBg
                                                ) {
                                                    Text(
                                                        text = "Verified Marking Scheme",
                                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                        color = NeonGreen,
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                    )
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = question.markingSchemeAnswer,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 15.sp,
                                                    fontFamily = FontFamily.Monospace
                                                ),
                                                color = TextWhite
                                            )

                                            Spacer(modifier = Modifier.height(10.dp))
                                            HorizontalDivider(color = DarkCardBorder, thickness = 1.dp)
                                            Spacer(modifier = Modifier.height(10.dp))

                                            Text(
                                                text = "Step-by-Step Working & Method Marks:",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = TextSecondary
                                            )

                                            Spacer(modifier = Modifier.height(6.dp))

                                            question.steps.forEach { step ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 4.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.Top
                                                ) {
                                                    Row(
                                                        modifier = Modifier.weight(1f),
                                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                        verticalAlignment = Alignment.Top
                                                    ) {
                                                        Surface(
                                                            shape = CircleShape,
                                                            color = DarkBackground,
                                                            modifier = Modifier.size(20.dp)
                                                        ) {
                                                            Box(contentAlignment = Alignment.Center) {
                                                                Text(
                                                                    text = "${step.stepNumber}",
                                                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                                                    color = TextSecondary
                                                                )
                                                            }
                                                        }
                                                        Column {
                                                            Text(
                                                                text = step.title,
                                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                                color = TextWhite
                                                            )
                                                            Text(
                                                                text = step.working,
                                                                style = MaterialTheme.typography.bodySmall.copy(
                                                                    fontFamily = FontFamily.Monospace,
                                                                    fontSize = 12.sp,
                                                                    lineHeight = 16.sp
                                                                ),
                                                                color = TextWhite.copy(alpha = 0.9f)
                                                            )
                                                        }
                                                    }

                                                    Surface(
                                                        shape = RoundedCornerShape(6.dp),
                                                        color = TagGreenBg,
                                                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)),
                                                        modifier = Modifier.padding(start = 6.dp)
                                                    ) {
                                                        Text(
                                                            text = step.markCode,
                                                            style = MaterialTheme.typography.labelSmall.copy(
                                                                fontWeight = FontWeight.Black,
                                                                fontSize = 10.sp
                                                            ),
                                                            color = NeonGreen,
                                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                        )
                                                    }
                                                }
                                            }

                                            Spacer(modifier = Modifier.height(8.dp))

                                            // Ask Tutor row inside solution
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End
                                            ) {
                                                TextButton(
                                                    onClick = { onAskTutor(question) },
                                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                                ) {
                                                    Row(
                                                        verticalAlignment = Alignment.CenterVertically,
                                                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.AutoMirrored.Filled.Chat,
                                                            contentDescription = null,
                                                            tint = NeonGreen,
                                                            modifier = Modifier.size(14.dp)
                                                        )
                                                        Text(
                                                            text = "Ask AI Tutor to explain",
                                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                            color = NeonGreen
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Snackbar confirmation for download
            if (showDownloadSnackbar) {
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { showDownloadSnackbar = false }) {
                            Text("OK", color = NeonGreen)
                        }
                    },
                    containerColor = DarkSurfaceVariant,
                    contentColor = TextWhite
                ) {
                    Text(snackbarMessage)
                }
            }
        }
    }
}
