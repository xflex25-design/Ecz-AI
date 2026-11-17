package com.example.data.repository

import com.example.data.api.GeminiClient
import com.example.data.db.*
import com.example.data.models.BestSixResult
import com.example.data.models.FormulaCard
import com.example.data.models.PastPaperQuestion
import com.example.data.models.SubjectGradeInput
import com.example.data.sample.ECZCurriculumData
import kotlinx.coroutines.flow.Flow

class ECZRepository(
    private val chatDao: ChatDao,
    private val studyNoteDao: StudyNoteDao,
    private val quizDao: QuizDao,
    private val studyTaskDao: StudyTaskDao,
    private val downloadedPaperDao: DownloadedPaperDao
) {
    // Downloaded Papers Offline Stream
    val downloadedPaperIds: Flow<List<String>> = downloadedPaperDao.getAllDownloadedPaperIds()
    val downloadedPapers: Flow<List<DownloadedPaperEntity>> = downloadedPaperDao.getAllDownloadedPapers()

    suspend fun saveDownloadedPaper(paper: com.example.data.models.ECZPastPaper) {
        downloadedPaperDao.insertDownloadedPaper(
            DownloadedPaperEntity(
                paperId = paper.id,
                subject = paper.subject,
                gradeLevel = paper.gradeLevel.displayName,
                year = paper.year,
                paperTitle = paper.title
            )
        )
    }

    suspend fun deleteDownloadedPaper(paperId: String) {
        downloadedPaperDao.deleteDownloadedPaper(paperId)
    }

    // Chat Stream
    val allChatMessages: Flow<List<ChatMessageEntity>> = chatDao.getAllMessages()

    suspend fun sendUserMessage(text: String, subject: String?, grade: String?): String {
        // Save user message to Room
        chatDao.insertMessage(
            ChatMessageEntity(
                isUser = true,
                message = text,
                subjectTag = subject
            )
        )

        // Request AI tutor explanation
        val result = GeminiClient.generateECZResponse(text, subject, grade)
        val aiResponse = result.getOrElse {
            "Unable to generate ECZ explanation at this time. Please check connection and try again."
        }

        // Save AI message to Room
        chatDao.insertMessage(
            ChatMessageEntity(
                isUser = false,
                message = aiResponse,
                subjectTag = subject
            )
        )

        return aiResponse
    }

    suspend fun clearChat() {
        chatDao.clearAllMessages()
    }

    // Study Notes Stream
    val allNotes: Flow<List<StudyNoteEntity>> = studyNoteDao.getAllNotes()

    suspend fun saveNote(title: String, subject: String, grade: String, content: String) {
        studyNoteDao.insertNote(
            StudyNoteEntity(
                title = title,
                subject = subject,
                gradeLevel = grade,
                content = content
            )
        )
    }

    suspend fun deleteNote(id: Long) {
        studyNoteDao.deleteNoteById(id)
    }

    // Quiz Results Stream
    val allQuizResults: Flow<List<QuizResultEntity>> = quizDao.getAllQuizResults()

    suspend fun saveQuizResult(
        subject: String,
        gradeLevel: String,
        paper: String,
        score: Int,
        total: Int,
        eczGrade: String
    ) {
        val percentage = if (total > 0) (score * 100) / total else 0
        quizDao.insertQuizResult(
            QuizResultEntity(
                subject = subject,
                gradeLevel = gradeLevel,
                paper = paper,
                score = score,
                totalQuestions = total,
                percentage = percentage,
                eczGrade = eczGrade
            )
        )
    }

    // Study Planner Tasks
    val allTasks: Flow<List<StudyTaskEntity>> = studyTaskDao.getAllTasks()

    suspend fun insertTask(title: String, subject: String, targetDate: String, priority: String = "Normal") {
        studyTaskDao.insertTask(
            StudyTaskEntity(
                title = title,
                subject = subject,
                targetDate = targetDate,
                priority = priority
            )
        )
    }

    suspend fun updateTaskStatus(task: StudyTaskEntity, isCompleted: Boolean) {
        studyTaskDao.updateTask(task.copy(isCompleted = isCompleted))
    }

    suspend fun deleteTask(id: Long) {
        studyTaskDao.deleteTaskById(id)
    }

    // Static & Curriculum Helpers
    fun getPastPapers(): List<PastPaperQuestion> = ECZCurriculumData.pastPaperQuestions

    fun getPastPaperDocuments(): List<com.example.data.models.ECZPastPaper> = ECZCurriculumData.pastPapers

    fun getPastPaperById(id: String): com.example.data.models.ECZPastPaper? = ECZCurriculumData.getPastPaperById(id)

    fun getFormulas(): List<FormulaCard> = ECZCurriculumData.revisionFormulas

    fun calculateBestSixPoints(inputs: List<SubjectGradeInput>): BestSixResult =
        ECZCurriculumData.calculateBestSix(inputs)
}
