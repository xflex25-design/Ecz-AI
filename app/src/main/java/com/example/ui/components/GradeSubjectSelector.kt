package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.ECZGradeLevel
import com.example.data.models.ECZSubjectsData
import com.example.ui.theme.*

/**
 * Material 3 button row for selecting an ECZ Grade Level.
 * Uses Material 3 [Button] (Filled) for the selected state and [OutlinedButton] for unselected states.
 */
@Composable
fun GradeLevelButtonRow(
    selectedGrade: ECZGradeLevel,
    onGradeSelected: (ECZGradeLevel) -> Unit,
    modifier: Modifier = Modifier,
    grades: List<ECZGradeLevel> = listOf(
        ECZGradeLevel.GRADE_7,
        ECZGradeLevel.GRADE_9,
        ECZGradeLevel.GRADE_12,
        ECZGradeLevel.GCE
    )
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .testTag("grade_level_button_row"),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(grades) { grade ->
            val isSelected = grade == selectedGrade
            if (isSelected) {
                Button(
                    onClick = { onGradeSelected(grade) },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonGreen,
                        contentColor = Color.Black
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .testTag("m3_grade_button_${grade.name.lowercase()}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = grade.displayName,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        )
                    }
                }
            } else {
                OutlinedButton(
                    onClick = { onGradeSelected(grade) },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, DarkCardBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkSurfaceVariant,
                        contentColor = TextWhite
                    ),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .testTag("m3_grade_button_${grade.name.lowercase()}")
                ) {
                    Text(
                        text = grade.displayName,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        ),
                        color = TextWhite
                    )
                }
            }
        }
    }
}

/**
 * UI Component using Material 3 buttons that allows the user to select their grade level,
 * which then updates the state to show the relevant subject list.
 */
@Composable
fun GradeSubjectSelector(
    modifier: Modifier = Modifier,
    initialGrade: ECZGradeLevel = ECZGradeLevel.GRADE_12,
    selectedSubjects: Set<String> = emptySet(),
    onGradeChanged: ((ECZGradeLevel) -> Unit)? = null,
    onSubjectClick: ((String, ECZGradeLevel) -> Unit)? = null,
    onToggleSubject: ((String) -> Unit)? = null
) {
    var selectedGrade by remember { mutableStateOf(initialGrade) }

    // Derive the relevant subject list from the active grade level state
    val relevantSubjects = remember(selectedGrade) {
        ECZSubjectsData.getSubjectsForGrade(selectedGrade)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("grade_subject_selector_component")
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Select Grade Level",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = TextWhite
                )
                Text(
                    text = "ECZ Examination & Curriculum",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                    color = TextSecondary
                )
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = TagGreenBg,
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
            ) {
                Text(
                    text = "${relevantSubjects.size} subjects",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    color = NeonGreen,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Material 3 Grade Level Selection Buttons
        GradeLevelButtonRow(
            selectedGrade = selectedGrade,
            onGradeSelected = { newGrade ->
                selectedGrade = newGrade
                onGradeChanged?.invoke(newGrade)
            },
            modifier = Modifier.padding(horizontal = 12.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Grade Info Banner
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = DarkSurface,
            border = BorderStroke(1.dp, DarkCardBorder),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = null,
                        tint = NeonGreen,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = selectedGrade.examName,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp
                        ),
                        color = TextWhite
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Relevant Subject List
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = false)
                .testTag("relevant_subjects_list"),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(relevantSubjects) { subject ->
                val isSelected = selectedSubjects.contains(subject)

                SubjectListItemCard(
                    subjectName = subject,
                    grade = selectedGrade,
                    isSelected = isSelected,
                    onCardClick = { onSubjectClick?.invoke(subject, selectedGrade) },
                    onToggle = onToggleSubject?.let { toggle -> { toggle(subject) } }
                )
            }
        }
    }
}

@Composable
private fun SubjectListItemCard(
    subjectName: String,
    grade: ECZGradeLevel,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    onToggle: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(
            width = 1.dp,
            color = if (isSelected) NeonGreen.copy(alpha = 0.6f) else DarkCardBorder
        ),
        modifier = modifier
            .fillMaxWidth()
            .testTag("subject_item_${subjectName.replace(" ", "_")}")
            .clickable(onClick = onCardClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // If onToggle is provided, show square checkbox
                if (onToggle != null) {
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = if (isSelected) NeonGreen else Color.Transparent,
                        border = BorderStroke(
                            width = 2.dp,
                            color = if (isSelected) NeonGreen else Color(0xFF8E9FA9)
                        ),
                        modifier = Modifier
                            .size(22.dp)
                            .clickable(onClick = onToggle)
                    ) {
                        if (isSelected) {
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
                } else {
                    // Subject Icon badge
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = TagGreenBg,
                        border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.35f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val icon = when {
                                subjectName.contains("Math", ignoreCase = true) -> Icons.Default.Calculate
                                subjectName.contains("English", ignoreCase = true) || subjectName.contains("Literature", ignoreCase = true) -> Icons.AutoMirrored.Filled.MenuBook
                                subjectName.contains("Bio", ignoreCase = true) -> Icons.Default.Spa
                                subjectName.contains("Chem", ignoreCase = true) || subjectName.contains("Science", ignoreCase = true) -> Icons.Default.Science
                                subjectName.contains("Phys", ignoreCase = true) -> Icons.Default.Bolt
                                subjectName.contains("Civic", ignoreCase = true) || subjectName.contains("Social", ignoreCase = true) -> Icons.Default.AccountBalance
                                subjectName.contains("Commerce", ignoreCase = true) || subjectName.contains("Business", ignoreCase = true) || subjectName.contains("Accounts", ignoreCase = true) -> Icons.Default.Paid
                                subjectName.contains("Computer", ignoreCase = true) || subjectName.contains("Technology", ignoreCase = true) -> Icons.Default.Computer
                                subjectName.contains("Geo", ignoreCase = true) -> Icons.Default.Public
                                subjectName.contains("History", ignoreCase = true) -> Icons.Default.HistoryEdu
                                else -> Icons.Default.Book
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = subjectName,
                                tint = NeonGreen,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Column {
                    Text(
                        text = subjectName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 15.sp
                        ),
                        color = TextWhite
                    )
                    Text(
                        text = "${grade.displayName} • ECZ Syllabus",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = TextSecondary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = TagGreenBg,
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f)),
                modifier = Modifier.clickable(onClick = onCardClick)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "View",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NeonGreen
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View",
                        tint = NeonGreen,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
