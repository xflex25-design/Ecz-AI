package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
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
import com.example.data.models.ECZSubjectsData
import com.example.ui.components.GradeLevelButtonRow
import com.example.ui.theme.*
import com.example.ui.viewmodels.ECZViewModel

@Composable
fun ChooseSubjectsScreen(
    viewModel: ECZViewModel,
    onFinish: () -> Unit,
    onSubjectClick: (String, ECZGradeLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    // 3 Tabs as shown in screenshots: Grade 12 (ECZ), Grade 9 (ECZ), Grade 7 (ECZ)
    var selectedGradeTab by remember { mutableStateOf(ECZGradeLevel.GRADE_12) }

    val userChosenSubjects by viewModel.userChosenSubjects.collectAsStateWithLifecycle()

    val currentSubjects = remember(selectedGradeTab) {
        ECZSubjectsData.getSubjectsForGrade(selectedGradeTab)
    }

    val selectedCount = userChosenSubjects.size

    Scaffold(
        topBar = {
            Surface(
                color = DarkBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 4.dp)
                ) {
                    // Back button & Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        IconButton(
                            onClick = onFinish,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = TextWhite
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Choose your subjects.",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            letterSpacing = (-0.5).sp
                        ),
                        color = TextWhite,
                        modifier = Modifier.padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Material 3 Buttons: Grade 12 (ECZ), Grade 9 (ECZ), Grade 7 (ECZ), GCE
                    GradeLevelButtonRow(
                        selectedGrade = selectedGradeTab,
                        onGradeSelected = { selectedGradeTab = it },
                        grades = listOf(
                            ECZGradeLevel.GRADE_12,
                            ECZGradeLevel.GRADE_9,
                            ECZGradeLevel.GRADE_7,
                            ECZGradeLevel.GCE
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Divider below buttons
                    HorizontalDivider(
                        color = DarkCardBorder,
                        thickness = 1.dp
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = DarkBackground,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // "0 of 6 selected" counter text matching screenshot
                    Text(
                        text = "$selectedCount of 6 selected",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Normal,
                            fontSize = 15.sp
                        ),
                        color = TextWhite
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Finish pill button matching screenshot
                    Surface(
                        shape = RoundedCornerShape(26.dp),
                        color = if (selectedCount > 0) NeonGreen else Color(0xFF2A363E),
                        modifier = Modifier
                            .width(130.dp)
                            .height(46.dp)
                            .testTag("choose_subjects_finish_button")
                            .clickable(onClick = onFinish)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Finish",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                ),
                                color = if (selectedCount > 0) Color.Black else TextSecondary
                            )
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
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            items(currentSubjects) { subject ->
                val isChecked = userChosenSubjects.contains(subject)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("subject_row_$subject")
                        .clickable {
                            // Toggle selection
                            viewModel.toggleSubjectChoice(subject)
                        }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        // Square Checkbox matching screenshot
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = if (isChecked) NeonGreen else Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 2.dp,
                                color = if (isChecked) NeonGreen else Color(0xFF8E9FA9)
                            ),
                            modifier = Modifier
                                .size(22.dp)
                                .clickable { viewModel.toggleSubjectChoice(subject) }
                        ) {
                            if (isChecked) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = Color.Black,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = subject,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 17.sp,
                                fontWeight = if (isChecked) FontWeight.SemiBold else FontWeight.Normal
                            ),
                            color = TextWhite
                        )
                    }

                    // Direct "View / Learn" action chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TagGreenBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f)),
                        modifier = Modifier.clickable {
                            onSubjectClick(subject, selectedGradeTab)
                        }
                    ) {
                        Text(
                            text = "View",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = NeonGreen,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
