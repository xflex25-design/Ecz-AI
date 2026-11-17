package com.example

import com.example.data.models.ECZGradeLevel
import com.example.data.models.ECZSubjectsData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GradeSubjectSelectorTest {

    @Test
    fun testGrade12SubjectsReturnedWhenGrade12Selected() {
        val subjects = ECZSubjectsData.getSubjectsForGrade(ECZGradeLevel.GRADE_12)
        assertTrue(subjects.contains("Mathematics"))
        assertTrue(subjects.contains("Biology"))
        assertTrue(subjects.contains("Chemistry (5070)"))
        assertTrue(subjects.contains("Physics (5054)"))
        assertTrue(subjects.contains("English"))
        assertEquals(35, subjects.size)
    }

    @Test
    fun testGrade9SubjectsReturnedWhenGrade9Selected() {
        val subjects = ECZSubjectsData.getSubjectsForGrade(ECZGradeLevel.GRADE_9)
        assertTrue(subjects.contains("Integrated Science"))
        assertTrue(subjects.contains("Social Studies"))
        assertTrue(subjects.contains("Mathematics"))
        assertTrue(subjects.contains("English"))
        assertEquals(20, subjects.size)
    }

    @Test
    fun testGrade7SubjectsReturnedWhenGrade7Selected() {
        val subjects = ECZSubjectsData.getSubjectsForGrade(ECZGradeLevel.GRADE_7)
        assertTrue(subjects.contains("Special Paper One"))
        assertTrue(subjects.contains("Special Paper Two"))
        assertTrue(subjects.contains("Integrated Science"))
        assertEquals(12, subjects.size)
    }

    @Test
    fun testStateSwitchingUpdatesSubjectList() {
        var currentGrade = ECZGradeLevel.GRADE_7
        var subjects = ECZSubjectsData.getSubjectsForGrade(currentGrade)
        assertEquals(12, subjects.size)

        // User switches grade to Grade 9
        currentGrade = ECZGradeLevel.GRADE_9
        subjects = ECZSubjectsData.getSubjectsForGrade(currentGrade)
        assertEquals(20, subjects.size)
        assertTrue(subjects.contains("Social Studies"))

        // User switches grade to Grade 12
        currentGrade = ECZGradeLevel.GRADE_12
        subjects = ECZSubjectsData.getSubjectsForGrade(currentGrade)
        assertEquals(35, subjects.size)
        assertTrue(subjects.contains("Chemistry (5070)"))
    }

    @Test
    fun testInitialChosenSubjectsEmptyAndToggle() {
        val selectedSubjects = mutableSetOf<String>()
        assertTrue(selectedSubjects.isEmpty())

        // User selects subjects
        selectedSubjects.add("Mathematics")
        selectedSubjects.add("Science")
        assertEquals(2, selectedSubjects.size)
        assertTrue(selectedSubjects.contains("Mathematics"))
        assertTrue(selectedSubjects.contains("Science"))

        // User unselects a subject
        selectedSubjects.remove("Science")
        assertEquals(1, selectedSubjects.size)
        assertTrue(selectedSubjects.contains("Mathematics"))
    }
}
