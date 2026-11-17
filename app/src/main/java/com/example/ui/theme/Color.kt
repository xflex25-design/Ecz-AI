package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Reference Dark Theme (Exact match to uploaded design mockup)
val DarkBackground = Color(0xFF0B1013)
val DarkSurface = Color(0xFF121A1F)
val DarkSurfaceVariant = Color(0xFF19232A)
val DarkCardBorder = Color(0xFF1F2B33)

// Glowing Neon Emerald / Mint Green Accent
val NeonGreen = Color(0xFF00E676)
val NeonGreenDark = Color(0xFF00C853)
val NeonGreenContainer = Color(0xFF0C2E22)
val NeonGreenBorder = Color(0xFF00E676).copy(alpha = 0.4f)

// Texts
val TextWhite = Color(0xFFFFFFFF)
val TextSecondary = Color(0xFF8E9FA9)
val TextMuted = Color(0xFF5B6E7B)

// Badges & States
val TagGreenBg = Color(0xFF0A2E20)
val TagGreenText = Color(0xFF00E676)
val ErrorRed = Color(0xFFEF4444)
val ErrorRedBg = Color(0xFF2E1215)
val WarningAmber = Color(0xFFF59E0B)

// Legacy Aliases for seamless backward compatibility across app
val SolidBlack = DarkBackground
val SolidBlackDark = DarkBackground
val SolidBlackCard = DarkSurface
val SolidBlackBorder = DarkCardBorder

val PureWhite = TextWhite
val PureWhiteCard = DarkSurface
val PureWhiteBorder = DarkCardBorder

val LightSolidGreen = NeonGreen
val LightSolidGreenContainer = NeonGreenContainer
val StylusYellowAccent = NeonGreen
val StylusYellowContainer = NeonGreenContainer

val EmeraldGreenPrimary = NeonGreen
val EmeraldGreenDark = NeonGreenDark
val EmeraldGreenLight = NeonGreenContainer

val CopperGoldSecondary = NeonGreen
val CopperGoldLight = NeonGreen
val CopperGoldContainer = NeonGreenContainer

val AcademicBlueTertiary = Color(0xFF3B82F6)
val AcademicBlueContainer = Color(0xFF1E293B)

val BackgroundDark = DarkBackground
val SurfaceDark = DarkSurface
val SurfaceVariantDark = DarkSurfaceVariant
val TextPrimaryDark = TextWhite
val TextSecondaryDark = TextSecondary
val OutlineDark = DarkCardBorder

val BackgroundLight = DarkBackground
val SurfaceLight = DarkSurface
val SurfaceVariantLight = DarkSurfaceVariant
val TextPrimaryLight = TextWhite
val TextSecondaryLight = TextSecondary
val OutlineLight = DarkCardBorder

val DistinctionGold = NeonGreen
val MeritBlue = Color(0xFF38BDF8)
val CreditGreen = NeonGreen
val PassAmber = WarningAmber
val FailRed = ErrorRed
