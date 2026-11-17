package com.example.data.models

enum class ECZGradeLevel(
    val displayName: String,
    val shortName: String,
    val examName: String,
    val isNewCurriculum: Boolean = false
) {
    GRADE_7("Grade 7", "Gr 7", "Primary School Leaving Exam (PSLE)"),
    GRADE_9("Grade 9", "Gr 9", "Junior Secondary School Leaving Exam (JSSLE)"),
    GRADE_12("Grade 12", "Gr 12", "School Certificate (ECZ G12)"),
    GCE("GCE", "GCE", "General Certificate of Education"),
    FORM_1("Form 1", "Form 1", "New Curriculum (Junior Secondary Form 1)", isNewCurriculum = true),
    FORM_2("Form 2", "Form 2", "New Curriculum (Junior Secondary Form 2)", isNewCurriculum = true),
    FORM_3("Form 3", "Form 3", "New Curriculum (Senior Secondary Form 3)", isNewCurriculum = true),
    FORM_4("Form 4", "Form 4", "New Curriculum (Senior Secondary Form 4)", isNewCurriculum = true)
}

data class MarkingStep(
    val stepNumber: Int,
    val title: String,
    val working: String,
    val markCode: String = "M1" // e.g. M1 (Method), A1 (Accuracy), B1 (Independent)
)

data class PastPaperQuestion(
    val id: String,
    val gradeLevel: ECZGradeLevel,
    val subject: String,
    val year: Int,
    val paper: String,
    val section: String,
    val questionNumber: String,
    val marks: Int,
    val topic: String,
    val questionText: String,
    val options: List<String> = emptyList(),
    val correctAnswerIndex: Int? = null,
    val markingSchemeAnswer: String,
    val stepByStepExplanation: String,
    val steps: List<MarkingStep> = emptyList()
)

data class ECZPastPaper(
    val id: String,
    val title: String,
    val subject: String,
    val gradeLevel: ECZGradeLevel,
    val year: Int,
    val paper: String, // e.g. "Paper 1", "Paper 2"
    val paperCode: String = "4024/1",
    val duration: String = "2 hours",
    val totalMarks: Int = 80,
    val instructions: String = "Answer all questions. Omission of essential working will result in loss of marks. Electronic calculators must NOT be used in this paper.",
    val questions: List<PastPaperQuestion> = emptyList(),
    val fileSize: String = "1.8 MB"
)

data class FormulaCard(
    val id: String,
    val subject: String,
    val gradeLevel: ECZGradeLevel,
    val topic: String,
    val title: String,
    val formula: String,
    val explanation: String,
    val example: String
)

data class SubjectGradeInput(
    val subjectName: String,
    val gradeValue: Int // 1 to 9 (1 is best, 9 is fail)
)

data class BestSixResult(
    val totalPoints: Int,
    val selectedSubjects: List<SubjectGradeInput>,
    val eligibilitySummary: String,
    val qualificationLevel: String
)

data class ChatMessage(
    val id: Long = 0,
    val isUser: Boolean,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val subjectTag: String? = null
)
