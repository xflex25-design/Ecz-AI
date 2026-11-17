package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.models.ECZGradeLevel
import com.example.ui.theme.*

@Composable
fun ECZHeader(
    selectedGrade: ECZGradeLevel,
    onGradeSelected: (ECZGradeLevel) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = DarkBackground,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp, bottom = 8.dp)
        ) {
            // Top Row: Logo & Title + "3 free scans left | Go unlimited ->" Pill
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: App Logo & Branding
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = TagGreenBg,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_ecz_logo),
                            contentDescription = "ECZ Logo",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ECZ AI",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.4.sp,
                                fontSize = 17.sp
                            ),
                            color = TextWhite
                        )
                        Text(
                            text = "Smart Revision. Better Results.",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = TextSecondary
                        )
                    }
                }

                // Right: 3 free scans left pill (matches mockup)
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = TagGreenBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.testTag("free_scans_upgrade_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "3 free scans left",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.5.sp
                            ),
                            color = TextWhite
                        )
                        Text(
                            text = "• Go unlimited",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.5.sp
                            ),
                            color = NeonGreen
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = NeonGreen,
                            modifier = Modifier.size(11.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Grade & Curriculum Selection Horizontal Scroll Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("grade_selection_toggle_row"),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val examGrades = listOf(
                    ECZGradeLevel.GRADE_7,
                    ECZGradeLevel.GRADE_9,
                    ECZGradeLevel.GRADE_12,
                    ECZGradeLevel.GCE
                )

                items(examGrades) { grade ->
                    val isSelected = selectedGrade == grade
                    GradePillItem(
                        grade = grade,
                        isSelected = isSelected,
                        onClick = { onGradeSelected(grade) }
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .height(20.dp)
                            .width(1.dp)
                            .background(DarkCardBorder)
                    )
                }

                val newCurriculumForms = listOf(
                    ECZGradeLevel.FORM_1,
                    ECZGradeLevel.FORM_2,
                    ECZGradeLevel.FORM_3,
                    ECZGradeLevel.FORM_4
                )

                items(newCurriculumForms) { form ->
                    val isSelected = selectedGrade == form
                    GradePillItem(
                        grade = form,
                        isSelected = isSelected,
                        onClick = { onGradeSelected(form) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GradePillItem(
    grade: ECZGradeLevel,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) TagGreenBg else DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(
            width = if (isSelected) 1.5.dp else 1.dp,
            color = if (isSelected) NeonGreen else DarkCardBorder
        ),
        modifier = Modifier
            .testTag("grade_toggle_pill_${grade.name.lowercase()}")
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 13.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(5.dp)
                        .clip(CircleShape)
                        .background(NeonGreen)
                )
            }
            Text(
                text = grade.displayName,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                    fontSize = 12.sp
                ),
                color = if (isSelected) NeonGreen else TextSecondary
            )
        }
    }
}
