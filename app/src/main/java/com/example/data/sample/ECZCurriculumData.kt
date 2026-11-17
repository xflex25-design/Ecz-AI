package com.example.data.sample

import com.example.data.models.ECZGradeLevel
import com.example.data.models.ECZPastPaper
import com.example.data.models.FormulaCard
import com.example.data.models.MarkingStep
import com.example.data.models.PastPaperQuestion
import com.example.data.models.SubjectGradeInput
import com.example.data.models.BestSixResult

object ECZCurriculumData {

    // 2023 Mathematics Paper 1 Questions
    private val g12Math2023P1Questions = listOf(
        PastPaperQuestion(
            id = "g12_math_2023_p1_q1",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 1",
            marks = 2,
            topic = "Indices & Powers",
            questionText = "Evaluate 27^(2/3) + 3^0.",
            options = listOf("10", "9", "7", "28"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "10",
            stepByStepExplanation = "Step 1: Express 27 as 3^3: (3^3)^(2/3) = 3^(3 * 2/3) = 3^2 = 9.\nStep 2: Any non-zero number to power 0 equals 1: 3^0 = 1.\nStep 3: Add results: 9 + 1 = 10.",
            steps = listOf(
                MarkingStep(1, "Simplify fractional index", "27^(2/3) = (3^3)^(2/3) = 3^2 = 9", "M1"),
                MarkingStep(2, "Zero index rule", "3^0 = 1", "B1"),
                MarkingStep(3, "Final sum", "9 + 1 = 10", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q2",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 2",
            marks = 2,
            topic = "Algebraic Expansions",
            questionText = "Simplify (3x - y)^2 - (3x + y)^2.",
            options = listOf("-12xy", "12xy", "0", "-6xy"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "-12xy",
            stepByStepExplanation = "Method 1: Expansion:\n(3x - y)^2 = 9x^2 - 6xy + y^2\n(3x + y)^2 = 9x^2 + 6xy + y^2\nSubtracting: (9x^2 - 6xy + y^2) - (9x^2 + 6xy + y^2) = -6xy - 6xy = -12xy.\n\nMethod 2: Difference of two squares a^2 - b^2 = (a - b)(a + b):\n[(3x - y) - (3x + y)][(3x - y) + (3x + y)] = (-2y)(6x) = -12xy.",
            steps = listOf(
                MarkingStep(1, "Expansion or difference of squares", "[(3x - y) - (3x + y)][(3x - y) + (3x + y)] or (9x^2 - 6xy + y^2) - (9x^2 + 6xy + y^2)", "M1"),
                MarkingStep(2, "Accurate simplification", "-12xy", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q3",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 3",
            marks = 2,
            topic = "Vectors in 2D",
            questionText = "Given that vector a = (3, -4), find the unit vector in the direction of vector a.",
            options = listOf("1/5 (3, -4)", "(3/25, -4/25)", "(1/3, -1/4)", "5 (3, -4)"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "1/5 (3, -4) or (3/5, -4/5)",
            stepByStepExplanation = "Step 1: Compute magnitude |a| = √(3^2 + (-4)^2) = √(9 + 16) = √25 = 5.\nStep 2: The unit vector â is given by a / |a| = 1/5 (3, -4) = (3/5, -4/5).",
            steps = listOf(
                MarkingStep(1, "Calculate magnitude", "|a| = √(3^2 + (-4)^2) = 5", "M1"),
                MarkingStep(2, "Unit vector formula", "â = 1/5 (3, -4) or (3/5, -4/5)", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q4",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 4",
            marks = 2,
            topic = "Exponential Equations",
            questionText = "Solve the equation 2^(2x - 1) = 1/32.",
            options = listOf("x = -2", "x = 2", "x = -3", "x = 3"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "x = -2",
            stepByStepExplanation = "Step 1: Express 1/32 as a power of 2: 1/32 = 1/(2^5) = 2^(-5).\nStep 2: Equate exponents because bases are equal: 2x - 1 = -5.\nStep 3: Solve for x: 2x = -5 + 1 = -4 => x = -2.",
            steps = listOf(
                MarkingStep(1, "Express RHS as power of 2", "1/32 = 2^(-5)", "M1"),
                MarkingStep(2, "Equate indices and solve", "2x - 1 = -5 => 2x = -4 => x = -2", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q5",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 5",
            marks = 4,
            topic = "Arithmetic Progressions (AP)",
            questionText = "The third term of an Arithmetic Progression (AP) is 10 and the seventh term is 26. Find:\n(a) The first term 'a' and common difference 'd'.\n(b) The sum of the first 15 terms (S_15).",
            options = listOf("a = 2, d = 4, S_15 = 450", "a = 4, d = 2, S_15 = 360", "a = 2, d = 3, S_15 = 345", "a = 1, d = 4, S_15 = 435"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "a = 2, d = 4; S_15 = 450",
            stepByStepExplanation = "For an AP, T_n = a + (n - 1)d.\n(a) T_3 = a + 2d = 10 (Eq 1)\nT_7 = a + 6d = 26 (Eq 2)\nSubtract Eq 1 from Eq 2: 4d = 16 => d = 4.\nSubstitute into Eq 1: a + 2(4) = 10 => a = 10 - 8 = 2.\n(b) Sum formula S_n = n/2 [2a + (n - 1)d]:\nS_15 = 15/2 [2(2) + (15 - 1)(4)] = 15/2 [4 + 56] = 15/2 * 60 = 15 * 30 = 450.",
            steps = listOf(
                MarkingStep(1, "Form simultaneous equations", "a + 2d = 10, a + 6d = 26", "M1"),
                MarkingStep(2, "Solve for d and a", "4d = 16 => d = 4, a = 2", "A1"),
                MarkingStep(3, "Sum formula substitution", "S_15 = 15/2 [2(2) + 14(4)]", "M1"),
                MarkingStep(4, "Evaluate sum", "S_15 = 450", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q6",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 6",
            marks = 3,
            topic = "Matrices",
            questionText = "Given that matrix A = [ 2  -1 ; 3   4 ], find:\n(a) The determinant of A, det(A).\n(b) The inverse matrix, A^(-1).",
            options = listOf("det(A) = 11, A^(-1) = 1/11 [ 4  1 ; -3  2 ]", "det(A) = 5, A^(-1) = 1/5 [ 4  1 ; -3  2 ]", "det(A) = 11, A^(-1) = 1/11 [ -4  -1 ; 3  -2 ]", "det(A) = 8, A^(-1) = [ 4  1 ; -3  2 ]"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "det(A) = 11; A^(-1) = 1/11 [ 4  1 ; -3  2 ]",
            stepByStepExplanation = "(a) det(A) = ad - bc = (2)(4) - (-1)(3) = 8 - (-3) = 8 + 3 = 11.\n(b) For matrix [a b ; c d], adjoint = [d -b ; -c a] = [4 1 ; -3 2].\nInverse A^(-1) = (1/det(A)) * adj(A) = 1/11 [ 4  1 ; -3  2 ].",
            steps = listOf(
                MarkingStep(1, "Determinant calculation", "det(A) = 2*4 - (-1)*3 = 11", "B1"),
                MarkingStep(2, "Adjoint matrix", "adj(A) = [ 4  1 ; -3  2 ]", "M1"),
                MarkingStep(3, "Inverse matrix", "A^(-1) = 1/11 [ 4  1 ; -3  2 ]", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q7",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 7",
            marks = 3,
            topic = "Functions & Composite Functions",
            questionText = "The functions f and g are defined by f(x) = 2x - 5 and g(x) = x^2 + 1. Find:\n(a) f^(-1)(x).\n(b) gf(3).",
            options = listOf("f^(-1)(x) = (x + 5)/2, gf(3) = 2", "f^(-1)(x) = (x - 5)/2, gf(3) = 10", "f^(-1)(x) = 2x + 5, gf(3) = 4", "f^(-1)(x) = (x + 5)/2, gf(3) = 5"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "f^(-1)(x) = (x + 5)/2; gf(3) = 2",
            stepByStepExplanation = "(a) Let y = 2x - 5. Interchange or solve for x: 2x = y + 5 => x = (y + 5)/2. Therefore f^(-1)(x) = (x + 5)/2.\n(b) First evaluate f(3): f(3) = 2(3) - 5 = 6 - 5 = 1.\nNow evaluate g(1): g(1) = 1^2 + 1 = 1 + 1 = 2. So gf(3) = 2.",
            steps = listOf(
                MarkingStep(1, "Invert f(x)", "y = 2x - 5 => x = (y + 5)/2 => f^(-1)(x) = (x + 5)/2", "B1"),
                MarkingStep(2, "Evaluate inner function f(3)", "f(3) = 2(3) - 5 = 1", "M1"),
                MarkingStep(3, "Evaluate outer function g(1)", "g(1) = 1^2 + 1 = 2", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q8",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 8",
            marks = 3,
            topic = "Probability",
            questionText = "The probability of Chipo passing Mathematics is 3/5 and the probability of her passing English is 2/3. Assuming the two events are independent, find the probability that she passes:\n(a) Both subjects.\n(b) Only one subject.",
            options = listOf("(a) 2/5, (b) 7/15", "(a) 6/15, (b) 8/15", "(a) 1/5, (b) 3/5", "(a) 2/5, (b) 1/3"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "(a) 2/5; (b) 7/15",
            stepByStepExplanation = "P(M) = 3/5, P(M') = 2/5. P(E) = 2/3, P(E') = 1/3.\n(a) Both: P(M ∩ E) = P(M) * P(E) = (3/5) * (2/3) = 6/15 = 2/5.\n(b) Only one: P(M and E') + P(M' and E) = (3/5 * 1/3) + (2/5 * 2/3) = 3/15 + 4/15 = 7/15.",
            steps = listOf(
                MarkingStep(1, "Both subjects multiplication", "P(M ∩ E) = 3/5 * 2/3 = 2/5", "B1"),
                MarkingStep(2, "Complement probabilities", "P(M') = 2/5, P(E') = 1/3", "M1"),
                MarkingStep(3, "Sum of mutually exclusive pairs", "(3/5 * 1/3) + (2/5 * 2/3) = 7/15", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q9",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 9",
            marks = 3,
            topic = "Calculus (Differentiation)",
            questionText = "Find the derivative dy/dx of the curve y = 4x^3 - 5x^2 + 7x - 9. Hence find the gradient of the curve at the point where x = 2.",
            options = listOf("dy/dx = 12x^2 - 10x + 7, gradient = 35", "dy/dx = 12x^2 - 10x + 7, gradient = 28", "dy/dx = 4x^2 - 5x + 7, gradient = 13", "dy/dx = 12x^2 - 5x, gradient = 38"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "dy/dx = 12x^2 - 10x + 7; gradient = 35",
            stepByStepExplanation = "Using power rule d/dx(ax^n) = n*a*x^(n-1):\ndy/dx = 4(3x^2) - 5(2x) + 7(1) - 0 = 12x^2 - 10x + 7.\nSubstitute x = 2:\ngradient = 12(2^2) - 10(2) + 7 = 12(4) - 20 + 7 = 48 - 20 + 7 = 35.",
            steps = listOf(
                MarkingStep(1, "Differentiate term by term", "dy/dx = 12x^2 - 10x + 7", "M1"),
                MarkingStep(2, "Substitute x = 2", "12(4) - 10(2) + 7", "M1"),
                MarkingStep(3, "Compute gradient", "Gradient = 35", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p1_q10",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 10",
            marks = 2,
            topic = "Calculus (Integration)",
            questionText = "Integrate with respect to x: ∫ (6x^2 - 4x + 3) dx.",
            options = listOf("2x^3 - 2x^2 + 3x + c", "3x^3 - 4x^2 + 3x + c", "12x - 4 + c", "2x^3 - 2x^2 + c"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "2x^3 - 2x^2 + 3x + c",
            stepByStepExplanation = "Apply power rule for indefinite integrals ∫ x^n dx = (x^(n+1))/(n+1) + c:\n∫ 6x^2 dx = 6 * (x^3)/3 = 2x^3\n∫ -4x dx = -4 * (x^2)/2 = -2x^2\n∫ 3 dx = 3x\nCombine and add constant of integration c: 2x^3 - 2x^2 + 3x + c.",
            steps = listOf(
                MarkingStep(1, "Integrate each term", "(6x^3)/3 - (4x^2)/2 + 3x", "M1"),
                MarkingStep(2, "Simplify with constant of integration", "2x^3 - 2x^2 + 3x + c", "A1")
            )
        )
    )

    // 2023 Mathematics Paper 2 Questions
    private val g12Math2023P2Questions = listOf(
        PastPaperQuestion(
            id = "g12_math_2023_p2_q1",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 2",
            section = "Section A",
            questionNumber = "Question 1",
            marks = 6,
            topic = "Quadratic Equations",
            questionText = "Solve the equation 3x^2 - 7x - 5 = 0, giving your answers correct to 2 decimal places.",
            options = listOf("x = 2.91 or x = -0.57", "x = 3.12 or x = -0.45", "x = 2.85 or x = -0.62", "x = 1.95 or x = -1.25"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "x = 2.91 or x = -0.57",
            stepByStepExplanation = "Using quadratic formula x = [-b ± √(b^2 - 4ac)] / (2a) where a = 3, b = -7, c = -5:\nx = [-(-7) ± √((-7)^2 - 4(3)(-5))] / (2 * 3)\nx = [7 ± √(49 + 60)] / 6 = [7 ± √109] / 6\nSince √109 ≈ 10.4403:\nx1 = (7 + 10.4403) / 6 = 17.4403 / 6 = 2.9067 ≈ 2.91\nx2 = (7 - 10.4403) / 6 = -3.4403 / 6 = -0.5734 ≈ -0.57.",
            steps = listOf(
                MarkingStep(1, "Formula substitution", "x = [7 ± √((-7)^2 - 4(3)(-5))] / (2*3)", "M1"),
                MarkingStep(2, "Discriminant calculation", "b^2 - 4ac = 49 + 60 = 109", "M1"),
                MarkingStep(3, "Root evaluation", "x = (7 ± 10.4403) / 6", "M1"),
                MarkingStep(4, "First root to 2 d.p.", "x = 2.91", "A1"),
                MarkingStep(5, "Second root to 2 d.p.", "x = -0.57", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p2_q2",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 2",
            section = "Section A",
            questionNumber = "Question 2",
            marks = 7,
            topic = "Trigonometry & Cosine Rule",
            questionText = "In triangle ABC, AB = 8 cm, BC = 11 cm, and angle ABC = 65°.\nCalculate:\n(a) The area of triangle ABC [2 marks].\n(b) The length AC, correct to 2 decimal places [3 marks].\n(c) The shortest distance from B to AC [2 marks].",
            options = listOf("Area = 39.88 cm^2, AC = 10.52 cm, Shortest dist = 7.58 cm", "Area = 44.00 cm^2, AC = 11.20 cm, Shortest dist = 8.12 cm", "Area = 36.50 cm^2, AC = 9.80 cm, Shortest dist = 7.10 cm", "Area = 40.15 cm^2, AC = 10.85 cm, Shortest dist = 7.40 cm"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "Area = 39.88 cm^2, AC = 10.52 cm, Shortest dist = 7.58 cm",
            stepByStepExplanation = "(a) Area = 1/2 * a * c * sin(B) = 1/2 * 11 * 8 * sin(65°) = 44 * 0.9063 = 39.88 cm^2.\n(b) Cosine rule: AC^2 = AB^2 + BC^2 - 2(AB)(BC)cos(65°)\nAC^2 = 8^2 + 11^2 - 2(8)(11)(0.4226) = 64 + 121 - 176(0.4226) = 185 - 74.38 = 110.62\nAC = √110.62 = 10.517 ≈ 10.52 cm.\n(c) Area = 1/2 * base * height = 1/2 * AC * h\n39.88 = 1/2 * 10.52 * h => h = (2 * 39.88) / 10.52 = 79.76 / 10.52 = 7.58 cm.",
            steps = listOf(
                MarkingStep(1, "Area formula application", "Area = 1/2 * 8 * 11 * sin(65°) = 39.88 cm^2", "M1"),
                MarkingStep(2, "Cosine rule formulation", "AC^2 = 8^2 + 11^2 - 2(8)(11)cos(65°)", "M1"),
                MarkingStep(3, "Evaluate length AC", "AC = √110.62 = 10.52 cm", "A1"),
                MarkingStep(4, "Shortest distance formula", "h = (2 * Area) / AC = (2 * 39.88) / 10.52", "M1"),
                MarkingStep(5, "Evaluate shortest distance", "h = 7.58 cm", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2023_p2_q3",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2023,
            paper = "Paper 2",
            section = "Section A",
            questionNumber = "Question 3",
            marks = 5,
            topic = "Earth Geometry",
            questionText = "Two towns P and Q lie on latitude 40°N. Town P is on longitude 25°E and town Q is on longitude 35°W.\n(a) Find the difference in longitude between P and Q.\n(b) Calculate the distance between P and Q along the circle of latitude in nautical miles (nm). [Take R = 3437 nm or 1° along great circle = 60 nm].",
            options = listOf("(a) 60°, (b) 2757.8 nm", "(a) 10°, (b) 459.6 nm", "(a) 60°, (b) 3600 nm", "(a) 50°, (b) 2298.1 nm"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "(a) 60°; (b) 2757.8 nm",
            stepByStepExplanation = "(a) Longitude difference θ = 25°E + 35°W = 60° (add because they lie on opposite sides of Prime Meridian).\n(b) Distance along a parallel of latitude = 60 * θ * cos(lat) nautical miles.\nDistance = 60 * 60 * cos(40°) = 3600 * 0.766044 = 2757.76 ≈ 2757.8 nm.",
            steps = listOf(
                MarkingStep(1, "Difference in longitude", "θ = 25° + 35° = 60°", "B1"),
                MarkingStep(2, "Nautical mile formula along parallel", "Distance = 60 * θ * cos(latitude)", "M1"),
                MarkingStep(3, "Calculate distance", "3600 * cos(40°) = 2757.8 nm", "A1")
            )
        )
    )

    // 2022 Mathematics Paper 1 Questions
    private val g12Math2022P1Questions = listOf(
        PastPaperQuestion(
            id = "g12_math_2022_p1_q1",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2022,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 1",
            marks = 2,
            topic = "Algebraic Expressions",
            questionText = "Simplify (x - 2)(x + 5) - x(x - 3).",
            options = listOf("6x - 10", "6x + 10", "-10", "2x^2 + 6x - 10"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "6x - 10",
            stepByStepExplanation = "Expand (x - 2)(x + 5) = x^2 + 5x - 2x - 10 = x^2 + 3x - 10.\nExpand x(x - 3) = x^2 - 3x.\nSubtract: (x^2 + 3x - 10) - (x^2 - 3x) = x^2 - x^2 + 3x - (-3x) - 10 = 6x - 10.",
            steps = listOf(
                MarkingStep(1, "Expand brackets", "x^2 + 3x - 10 - x^2 + 3x", "M1"),
                MarkingStep(2, "Collect like terms", "6x - 10", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2022_p1_q2",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2022,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 2",
            marks = 3,
            topic = "Algebraic Fractions",
            questionText = "Express 3/(x + 2) - 2/(x - 1) as a single fraction in its simplest form.",
            options = listOf("(x - 7) / ((x + 2)(x - 1))", "(x + 7) / ((x + 2)(x - 1))", "(x - 1) / ((x + 2)(x - 1))", "1 / (2x + 1)"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "(x - 7) / ((x + 2)(x - 1))",
            stepByStepExplanation = "Common denominator is (x + 2)(x - 1).\n[3(x - 1) - 2(x + 2)] / [(x + 2)(x - 1)]\n= [3x - 3 - 2x - 4] / [(x + 2)(x - 1)]\n= (x - 7) / [(x + 2)(x - 1)].",
            steps = listOf(
                MarkingStep(1, "Common denominator", "LCM = (x + 2)(x - 1)", "M1"),
                MarkingStep(2, "Numerator expansion", "3(x - 1) - 2(x + 2) = 3x - 3 - 2x - 4", "M1"),
                MarkingStep(3, "Simplified fraction", "(x - 7) / ((x + 2)(x - 1))", "A1")
            )
        ),
        PastPaperQuestion(
            id = "g12_math_2022_p1_q3",
            gradeLevel = ECZGradeLevel.GRADE_12,
            subject = "Mathematics",
            year = 2022,
            paper = "Paper 1",
            section = "Section A",
            questionNumber = "Question 3",
            marks = 4,
            topic = "Geometric Progressions (GP)",
            questionText = "For a Geometric Progression (GP), the first term is 64 and the fourth term is 8. Find:\n(a) The common ratio 'r'.\n(b) The tenth term, leaving your answer in index form.\n(c) The sum to infinity (S_∞).",
            options = listOf("r = 1/2, T_10 = 1/8, S_∞ = 128", "r = 1/4, T_10 = 1/16, S_∞ = 96", "r = 1/2, T_10 = 1/16, S_∞ = 64", "r = 2, T_10 = 32768, S_∞ = undefined"),
            correctAnswerIndex = 0,
            markingSchemeAnswer = "r = 1/2; T_10 = 1/8; S_∞ = 128",
            stepByStepExplanation = "(a) T_4 = a * r^3 = 64 * r^3 = 8 => r^3 = 8/64 = 1/8 => r = 1/2.\n(b) T_10 = a * r^9 = 64 * (1/2)^9 = 2^6 * 2^(-9) = 2^(-3) = 1/8.\n(c) S_∞ = a / (1 - r) = 64 / (1 - 1/2) = 64 / (1/2) = 128.",
            steps = listOf(
                MarkingStep(1, "Calculate common ratio r", "64 * r^3 = 8 => r = 1/2", "B1"),
                MarkingStep(2, "Calculate tenth term", "T_10 = 64 * (1/2)^9 = 1/8", "M1"),
                MarkingStep(3, "Sum to infinity formula", "S_∞ = 64 / (1 - 0.5) = 128", "A1")
            )
        )
    )

    // Official ECZ Past Papers Full Documents
    val pastPapers: List<ECZPastPaper> = listOf(
        ECZPastPaper(
            id = "g12_math_2023_p1",
            title = "2023 Mathematics Paper 1 (Non-Calculator)",
            subject = "Mathematics",
            gradeLevel = ECZGradeLevel.GRADE_12,
            year = 2023,
            paper = "Paper 1",
            paperCode = "4024/1",
            duration = "2 hours",
            totalMarks = 80,
            instructions = "Answer all questions. Write your answers in the spaces provided. Omission of essential working will result in loss of marks. Electronic calculators must NOT be used in this paper.",
            questions = g12Math2023P1Questions,
            fileSize = "2.1 MB"
        ),
        ECZPastPaper(
            id = "g12_math_2023_p2",
            title = "2023 Mathematics Paper 2 (Calculator Allowed)",
            subject = "Mathematics",
            gradeLevel = ECZGradeLevel.GRADE_12,
            year = 2023,
            paper = "Paper 2",
            paperCode = "4024/2",
            duration = "2 hours 30 mins",
            totalMarks = 100,
            instructions = "Answer all questions in Section A and any four questions from Section B. Silent non-programmable electronic calculators may be used. Mathematical tables are provided.",
            questions = g12Math2023P2Questions,
            fileSize = "2.8 MB"
        ),
        ECZPastPaper(
            id = "g12_math_2022_p1",
            title = "2022 Mathematics Paper 1 (Non-Calculator)",
            subject = "Mathematics",
            gradeLevel = ECZGradeLevel.GRADE_12,
            year = 2022,
            paper = "Paper 1",
            paperCode = "4024/1",
            duration = "2 hours",
            totalMarks = 80,
            instructions = "Answer all questions. Omission of essential working will result in loss of marks. Electronic calculators must NOT be used in this paper.",
            questions = g12Math2022P1Questions,
            fileSize = "1.9 MB"
        )
    )

    // Flat list of all questions across papers for search & filter
    val pastPaperQuestions: List<PastPaperQuestion> = pastPapers.flatMap { it.questions }

    val revisionFormulas: List<FormulaCard> = emptyList()

    fun getPastPapersForGradeAndSubject(grade: ECZGradeLevel, subject: String): List<ECZPastPaper> {
        return pastPapers.filter {
            it.gradeLevel == grade && (subject.isBlank() || subject.equals("All Subjects", ignoreCase = true) || it.subject.equals(subject, ignoreCase = true))
        }
    }

    fun getPastPaperById(id: String): ECZPastPaper? {
        return pastPapers.find { it.id == id }
    }

    fun calculateBestSix(inputs: List<SubjectGradeInput>): BestSixResult {
        val sorted = inputs.sortedBy { it.gradeValue }
        val bestSix = sorted.take(6)
        val total = bestSix.sumOf { it.gradeValue }

        val (qualification, summary) = when {
            total in 6..10 -> Pair(
                "Outstanding Distinction (6–10 Pts)",
                "Exceptional performance! Eligible for premier faculties at UNZA and CBU."
            )
            total in 11..16 -> Pair(
                "Merit Level (11–16 Pts)",
                "Excellent academic standing! Qualified for university degree programmes."
            )
            total in 17..24 -> Pair(
                "Credit Level (17–24 Pts)",
                "Solid School Certificate pass with credits."
            )
            total in 25..36 -> Pair(
                "Satisfactory Level (25–36 Pts)",
                "School Certificate obtained. Eligible for TEVETA diploma programmes."
            )
            else -> Pair(
                "GCE Improvement Recommended",
                "Total points exceed 36. Upgrade 2 to 3 subjects to credits."
            )
        }

        return BestSixResult(
            totalPoints = total,
            selectedSubjects = bestSix,
            eligibilitySummary = summary,
            qualificationLevel = qualification
        )
    }
}
