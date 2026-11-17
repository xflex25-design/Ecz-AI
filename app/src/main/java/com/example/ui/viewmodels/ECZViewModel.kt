package com.example.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.db.ChatMessageEntity
import com.example.data.db.QuizResultEntity
import com.example.data.db.StudyNoteEntity
import com.example.data.db.StudyTaskEntity
import com.example.data.models.BestSixResult
import com.example.data.models.ECZGradeLevel
import com.example.data.models.ECZPastPaper
import com.example.data.models.FormulaCard
import com.example.data.models.PastPaperQuestion
import com.example.data.models.SubjectGradeInput
import com.example.data.repository.ECZRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ECZViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = ECZRepository(
        chatDao = db.chatDao(),
        studyNoteDao = db.studyNoteDao(),
        quizDao = db.quizDao(),
        studyTaskDao = db.studyTaskDao(),
        downloadedPaperDao = db.downloadedPaperDao()
    )

    // Current Grade Level Selection
    private val _selectedGrade = MutableStateFlow(ECZGradeLevel.GRADE_12)
    val selectedGrade: StateFlow<ECZGradeLevel> = _selectedGrade.asStateFlow()

    fun setGrade(grade: ECZGradeLevel) {
        _selectedGrade.value = grade
    }

    // User's chosen subjects for learning - starts empty until user selects them
    private val prefs = application.getSharedPreferences("ecz_user_prefs", android.content.Context.MODE_PRIVATE)

    private val _userChosenSubjects = MutableStateFlow<Set<String>>(
        prefs.getStringSet("user_selected_subjects", emptySet()) ?: emptySet()
    )
    val userChosenSubjects: StateFlow<Set<String>> = _userChosenSubjects.asStateFlow()

    fun toggleSubjectChoice(subject: String) {
        val current = _userChosenSubjects.value.toMutableSet()
        if (current.contains(subject)) {
            current.remove(subject)
        } else {
            current.add(subject)
        }
        _userChosenSubjects.value = current
        prefs.edit().putStringSet("user_selected_subjects", current).apply()
    }

    fun setUserChosenSubjects(subjects: Set<String>) {
        _userChosenSubjects.value = subjects
        prefs.edit().putStringSet("user_selected_subjects", subjects).apply()
    }

    fun clearUserChosenSubjects() {
        _userChosenSubjects.value = emptySet()
        prefs.edit().remove("user_selected_subjects").apply()
    }

    // Chat / AI Tutor State
    val chatMessages: StateFlow<List<ChatMessageEntity>> = repository.allChatMessages
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: StateFlow<Boolean> = _isGenerating.asStateFlow()

    private val _chatInputText = MutableStateFlow("")
    val chatInputText: StateFlow<String> = _chatInputText.asStateFlow()

    private val _chatSubjectFilter = MutableStateFlow("General Syllabus")
    val chatSubjectFilter: StateFlow<String> = _chatSubjectFilter.asStateFlow()

    fun updateChatInput(text: String) {
        _chatInputText.value = text
    }

    fun setChatSubject(subject: String) {
        _chatSubjectFilter.value = subject
    }

    fun sendChatMessage(customPrompt: String? = null) {
        val prompt = customPrompt ?: _chatInputText.value.trim()
        if (prompt.isBlank()) return

        if (customPrompt == null) {
            _chatInputText.value = ""
        }

        viewModelScope.launch {
            _isGenerating.value = true
            try {
                repository.sendUserMessage(
                    text = prompt,
                    subject = _chatSubjectFilter.value,
                    grade = _selectedGrade.value.displayName
                )
            } finally {
                _isGenerating.value = false
            }
        }
    }

    fun clearChatHistory() {
        viewModelScope.launch {
            repository.clearChat()
        }
    }

    // AI Scanner State
    private val _scannedBitmap = MutableStateFlow<android.graphics.Bitmap?>(null)
    val scannedBitmap: StateFlow<android.graphics.Bitmap?> = _scannedBitmap.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _scannerResult = MutableStateFlow<String?>(null)
    val scannerResult: StateFlow<String?> = _scannerResult.asStateFlow()

    private val _scannerSubject = MutableStateFlow("Mathematics")
    val scannerSubject: StateFlow<String> = _scannerSubject.asStateFlow()

    fun setScannedBitmap(bitmap: android.graphics.Bitmap?) {
        _scannedBitmap.value = bitmap
        _scannerResult.value = null
    }

    fun setScannerSubject(subject: String) {
        _scannerSubject.value = subject
    }

    fun solveScannedQuestion(customPrompt: String? = null) {
        val bitmap = _scannedBitmap.value
        viewModelScope.launch {
            _isScanning.value = true
            try {
                val promptText = customPrompt ?: "Read, transcribe, and solve this ECZ examination question step-by-step with full working, formula, and final answer according to official Examinations Council of Zambia marking scheme criteria."
                val result = if (bitmap != null) {
                    com.example.data.api.GeminiClient.generateECZMultimodalResponse(
                        bitmap = bitmap,
                        prompt = promptText,
                        subjectContext = _scannerSubject.value,
                        gradeContext = _selectedGrade.value.displayName
                    )
                } else {
                    com.example.data.api.GeminiClient.generateECZResponse(
                        prompt = promptText,
                        subjectContext = _scannerSubject.value,
                        gradeContext = _selectedGrade.value.displayName
                    )
                }
                _scannerResult.value = result.getOrNull() ?: "Unable to analyze question image. Please try again."
            } finally {
                _isScanning.value = false
            }
        }
    }

    // Past Papers State
    private val _pastPaperSubjectFilter = MutableStateFlow("All Subjects")
    val pastPaperSubjectFilter: StateFlow<String> = _pastPaperSubjectFilter.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _revealedAnswers = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val revealedAnswers: StateFlow<Map<String, Boolean>> = _revealedAnswers.asStateFlow()

    fun setPastPaperSubject(subject: String) {
        _pastPaperSubjectFilter.value = subject
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleAnswerRevealed(questionId: String) {
        val current = _revealedAnswers.value.toMutableMap()
        val isRevealed = current[questionId] ?: false
        current[questionId] = !isRevealed
        _revealedAnswers.value = current
    }

    // Past Papers Documents & Offline State
    val downloadedPaperIds: StateFlow<Set<String>> = repository.downloadedPaperIds
        .map { it.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    private val _selectedViewingPaper = MutableStateFlow<ECZPastPaper?>(null)
    val selectedViewingPaper: StateFlow<ECZPastPaper?> = _selectedViewingPaper.asStateFlow()

    private val _filterOnlyDownloaded = MutableStateFlow(false)
    val filterOnlyDownloaded: StateFlow<Boolean> = _filterOnlyDownloaded.asStateFlow()

    private val _downloadingPaperId = MutableStateFlow<String?>(null)
    val downloadingPaperId: StateFlow<String?> = _downloadingPaperId.asStateFlow()

    fun selectPaperForViewing(paper: ECZPastPaper?) {
        _selectedViewingPaper.value = paper
    }

    fun selectPaperById(paperId: String) {
        val paper = repository.getPastPaperById(paperId)
        _selectedViewingPaper.value = paper
    }

    fun toggleFilterOnlyDownloaded() {
        _filterOnlyDownloaded.value = !_filterOnlyDownloaded.value
    }

    fun downloadPaper(paper: ECZPastPaper) {
        viewModelScope.launch {
            _downloadingPaperId.value = paper.id
            delay(600) // Realistic offline download caching
            repository.saveDownloadedPaper(paper)
            _downloadingPaperId.value = null
        }
    }

    fun deleteDownloadedPaper(paperId: String) {
        viewModelScope.launch {
            repository.deleteDownloadedPaper(paperId)
        }
    }

    fun isPaperDownloaded(paperId: String): Boolean {
        return downloadedPaperIds.value.contains(paperId)
    }

    fun getPastPaperDocuments(
        grade: ECZGradeLevel = _selectedGrade.value,
        subject: String = _pastPaperSubjectFilter.value
    ): List<ECZPastPaper> {
        val all = repository.getPastPaperDocuments()
        val query = _searchQuery.value.trim().lowercase()
        val downloadedOnly = _filterOnlyDownloaded.value
        val downloadedIds = downloadedPaperIds.value

        return all.filter { paper ->
            paper.gradeLevel == grade &&
                    (subject == "All Subjects" || paper.subject.equals(subject, ignoreCase = true)) &&
                    (!downloadedOnly || downloadedIds.contains(paper.id)) &&
                    (query.isEmpty() ||
                            paper.title.lowercase().contains(query) ||
                            paper.paper.lowercase().contains(query) ||
                            paper.subject.lowercase().contains(query) ||
                            paper.year.toString().contains(query))
        }
    }

    fun getFilteredPastPapers(): List<PastPaperQuestion> {
        val all = repository.getPastPapers()
        val currentGrade = _selectedGrade.value
        val subject = _pastPaperSubjectFilter.value
        val query = _searchQuery.value.trim().lowercase()

        return all.filter { q ->
            q.gradeLevel == currentGrade &&
                    (subject == "All Subjects" || q.subject.equals(subject, ignoreCase = true)) &&
                    (query.isEmpty() ||
                            q.questionText.lowercase().contains(query) ||
                            q.topic.lowercase().contains(query) ||
                            q.paper.lowercase().contains(query))
        }
    }

    // Mock Exam State
    private val _quizQuestions = MutableStateFlow<List<PastPaperQuestion>>(emptyList())
    val quizQuestions: StateFlow<List<PastPaperQuestion>> = _quizQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedOptions = MutableStateFlow<Map<String, Int>>(emptyMap())
    val selectedOptions: StateFlow<Map<String, Int>> = _selectedOptions.asStateFlow()

    private val _isExamSubmitted = MutableStateFlow(false)
    val isExamSubmitted: StateFlow<Boolean> = _isExamSubmitted.asStateFlow()

    private val _examScore = MutableStateFlow(0)
    val examScore: StateFlow<Int> = _examScore.asStateFlow()

    private val _examGradeResult = MutableStateFlow("")
    val examGradeResult: StateFlow<String> = _examGradeResult.asStateFlow()

    private val _examSecondsRemaining = MutableStateFlow(300) // 5 minutes
    val examSecondsRemaining: StateFlow<Int> = _examSecondsRemaining.asStateFlow()

    private var timerJob: Job? = null

    fun startMockExam(subject: String = "All") {
        val available = repository.getPastPapers().filter {
            it.options.isNotEmpty() && (subject == "All" || it.subject.contains(subject, ignoreCase = true))
        }
        _quizQuestions.value = if (available.isNotEmpty()) available else repository.getPastPapers().filter { it.options.isNotEmpty() }
        _currentQuestionIndex.value = 0
        _selectedOptions.value = emptyMap()
        _isExamSubmitted.value = false
        _examScore.value = 0
        _examGradeResult.value = ""
        _examSecondsRemaining.value = 300

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_examSecondsRemaining.value > 0 && !_isExamSubmitted.value) {
                delay(1000)
                _examSecondsRemaining.value -= 1
            }
            if (!_isExamSubmitted.value) {
                submitExam()
            }
        }
    }

    fun selectOption(questionId: String, optionIndex: Int) {
        if (_isExamSubmitted.value) return
        val current = _selectedOptions.value.toMutableMap()
        current[questionId] = optionIndex
        _selectedOptions.value = current
    }

    fun nextQuestion() {
        if (_currentQuestionIndex.value < _quizQuestions.value.size - 1) {
            _currentQuestionIndex.value += 1
        }
    }

    fun prevQuestion() {
        if (_currentQuestionIndex.value > 0) {
            _currentQuestionIndex.value -= 1
        }
    }

    fun submitExam() {
        timerJob?.cancel()
        _isExamSubmitted.value = true

        val questions = _quizQuestions.value
        var correctCount = 0
        questions.forEach { q ->
            val userChoice = _selectedOptions.value[q.id]
            if (userChoice != null && userChoice == q.correctAnswerIndex) {
                correctCount++
            }
        }
        _examScore.value = correctCount
        val total = questions.size
        val percentage = if (total > 0) (correctCount * 100) / total else 0

        val gradeStr = when {
            percentage >= 75 -> "Distinction 1"
            percentage >= 70 -> "Distinction 2"
            percentage >= 65 -> "Merit 3"
            percentage >= 60 -> "Merit 4"
            percentage >= 55 -> "Credit 5"
            percentage >= 50 -> "Credit 6"
            percentage >= 45 -> "Pass 7"
            percentage >= 40 -> "Pass 8"
            else -> "Unsatisfactory 9"
        }
        _examGradeResult.value = gradeStr

        viewModelScope.launch {
            repository.saveQuizResult(
                subject = "ECZ Multiple Choice Mock",
                gradeLevel = _selectedGrade.value.displayName,
                paper = "Paper 1 Simulation",
                score = correctCount,
                total = total,
                eczGrade = gradeStr
            )
        }
    }

    // Saved Notes & Bookmarks
    val savedNotes: StateFlow<List<StudyNoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveStudyNote(title: String, subject: String, content: String) {
        viewModelScope.launch {
            repository.saveNote(
                title = title,
                subject = subject,
                grade = _selectedGrade.value.displayName,
                content = content
            )
        }
    }

    fun deleteStudyNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // Study Tasks
    val studyTasks: StateFlow<List<StudyTaskEntity>> = repository.allTasks
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addStudyTask(title: String, subject: String, targetDate: String) {
        viewModelScope.launch {
            repository.insertTask(title, subject, targetDate)
        }
    }

    fun toggleTaskCompletion(task: StudyTaskEntity) {
        viewModelScope.launch {
            repository.updateTaskStatus(task, !task.isCompleted)
        }
    }

    fun deleteStudyTask(id: Long) {
        viewModelScope.launch {
            repository.deleteTask(id)
        }
    }

    // Formulas
    fun getFormulas(): List<FormulaCard> {
        val grade = _selectedGrade.value
        return repository.getFormulas().filter { it.gradeLevel == grade }
    }

    // Best 6 Points Calculator State
    private val _calculatorInputs = MutableStateFlow<List<SubjectGradeInput>>(
        listOf(
            SubjectGradeInput("Mathematics", 1),
            SubjectGradeInput("English Language", 2),
            SubjectGradeInput("Science (Physics/Chem)", 1),
            SubjectGradeInput("Biology", 2),
            SubjectGradeInput("Civic Education", 1),
            SubjectGradeInput("Geography", 2),
            SubjectGradeInput("Commerce", 3)
        )
    )
    val calculatorInputs: StateFlow<List<SubjectGradeInput>> = _calculatorInputs.asStateFlow()

    fun updateSubjectGrade(index: Int, newGrade: Int) {
        val current = _calculatorInputs.value.toMutableList()
        if (index in current.indices) {
            val old = current[index]
            current[index] = old.copy(gradeValue = newGrade.coerceIn(1, 9))
            _calculatorInputs.value = current
        }
    }

    fun addCalculatorSubject(name: String, grade: Int = 1) {
        val current = _calculatorInputs.value.toMutableList()
        current.add(SubjectGradeInput(name, grade.coerceIn(1, 9)))
        _calculatorInputs.value = current
    }

    fun removeCalculatorSubject(index: Int) {
        val current = _calculatorInputs.value.toMutableList()
        if (index in current.indices && current.size > 6) {
            current.removeAt(index)
            _calculatorInputs.value = current
        }
    }

    fun getBestSixResult(): BestSixResult {
        return repository.calculateBestSixPoints(_calculatorInputs.value)
    }
}
