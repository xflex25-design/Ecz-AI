package com.example.data.models

object ECZSubjectsData {

    val grade12Subjects = listOf(
        "Additional Mathematics",
        "Agricultural Science",
        "Art and Design",
        "Biology",
        "Chemistry (5070)",
        "Chitonga",
        "Cinyanja",
        "Civic Education",
        "Commerce",
        "Computer Studies",
        "Design and Technology",
        "English",
        "Fashion and Fabrics",
        "Food and Nutrition",
        "French",
        "Geography",
        "Geometrical and Mechanical Drawing",
        "History",
        "Home Management",
        "Icibemba",
        "Kiikaonde",
        "Literature in English",
        "Lunda",
        "Luvale",
        "Mathematics",
        "Metal Work",
        "Musical Arts Education",
        "Physical Education",
        "Physics (5054)",
        "Principles of Accounts",
        "Religious Education (2046)",
        "Religious Education (2044)",
        "Science",
        "Silozi",
        "Wood Work"
    )

    val grade9Subjects = listOf(
        "Agricultural Science",
        "Art and Design",
        "Business Studies",
        "Cinyanja",
        "Civics",
        "Computer Studies",
        "Design and Technology",
        "English",
        "French",
        "Geography",
        "History",
        "Home Economics",
        "Icibemba",
        "Integrated Science",
        "Mathematics",
        "Musical Arts Education",
        "Office Practice",
        "Physical Education",
        "Religious Education",
        "Social Studies"
    )

    val grade7Subjects = listOf(
        "Cinyanja",
        "Creative and Technology Studies",
        "English",
        "Expressive Arts",
        "Home Economics",
        "Icibemba",
        "Integrated Science",
        "Mathematics",
        "Social Studies",
        "Special Paper One",
        "Special Paper Two",
        "Technology Studies"
    )

    fun getSubjectsForGrade(grade: ECZGradeLevel): List<String> {
        return when (grade) {
            ECZGradeLevel.GRADE_7 -> grade7Subjects
            ECZGradeLevel.GRADE_9 -> grade9Subjects
            ECZGradeLevel.GRADE_12, ECZGradeLevel.GCE -> grade12Subjects
            ECZGradeLevel.FORM_1, ECZGradeLevel.FORM_2 -> grade9Subjects
            ECZGradeLevel.FORM_3, ECZGradeLevel.FORM_4 -> grade12Subjects
        }
    }
}
