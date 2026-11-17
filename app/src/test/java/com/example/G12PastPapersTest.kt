package com.example

import com.example.data.models.ECZGradeLevel
import com.example.data.sample.ECZCurriculumData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class G12PastPapersTest {

    @Test
    fun testG12MathematicsPastPapersExist() {
        val mathPapers = ECZCurriculumData.getPastPapersForGradeAndSubject(
            grade = ECZGradeLevel.GRADE_12,
            subject = "Mathematics"
        )

        assertTrue("Expected G12 Mathematics papers to be available", mathPapers.isNotEmpty())
        assertTrue(mathPapers.size >= 3)

        val p1_2023 = mathPapers.find { it.year == 2023 && it.paper == "Paper 1" }
        assertNotNull("Expected 2023 Mathematics Paper 1", p1_2023)
        assertEquals(80, p1_2023!!.totalMarks)
        assertEquals("4024/1", p1_2023.paperCode)
        assertEquals("2 hours", p1_2023.duration)
        assertTrue(p1_2023.questions.isNotEmpty())

        val p2_2023 = mathPapers.find { it.year == 2023 && it.paper == "Paper 2" }
        assertNotNull("Expected 2023 Mathematics Paper 2", p2_2023)
        assertEquals(100, p2_2023!!.totalMarks)
        assertEquals("4024/2", p2_2023.paperCode)
        assertEquals("2 hours 30 mins", p2_2023.duration)
    }

    @Test
    fun test2023Paper1QuestionsAndMarkingScheme() {
        val paper = ECZCurriculumData.getPastPaperById("g12_math_2023_p1")
        assertNotNull(paper)

        val q1 = paper!!.questions.find { it.questionNumber == "Question 1" }
        assertNotNull(q1)
        assertEquals("10", q1!!.markingSchemeAnswer)
        assertEquals(2, q1.marks)
        assertEquals("Indices & Powers", q1.topic)
        assertTrue(q1.steps.isNotEmpty())

        val q2 = paper.questions.find { it.questionNumber == "Question 2" }
        assertNotNull(q2)
        assertEquals("-12xy", q2!!.markingSchemeAnswer)

        val q6 = paper.questions.find { it.questionNumber == "Question 6" }
        assertNotNull(q6)
        assertEquals("Matrices", q6!!.topic)
    }

    @Test
    fun test2023Paper2QuestionsAndMarkingScheme() {
        val paper = ECZCurriculumData.getPastPaperById("g12_math_2023_p2")
        assertNotNull(paper)

        val q1 = paper!!.questions.find { it.questionNumber == "Question 1" }
        assertNotNull(q1)
        assertEquals("Quadratic Equations", q1!!.topic)
        assertEquals("x = 2.91 or x = -0.57", q1.markingSchemeAnswer)
        assertEquals(6, q1.marks)
        assertEquals(5, q1.steps.size)
    }

    @Test
    fun testNonG12OrNonMathSubjectHasEmptyPaperListInitially() {
        val nonMathPapers = ECZCurriculumData.getPastPapersForGradeAndSubject(
            grade = ECZGradeLevel.GRADE_7,
            subject = "Special Paper One"
        )
        assertTrue(nonMathPapers.isEmpty())
    }
}
