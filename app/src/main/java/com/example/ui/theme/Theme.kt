package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PruconDarkColorScheme = darkColorScheme(
  primary = EmeraldNeon,
  onPrimary = CyberBlack,
  primaryContainer = Color(0xFF064E3B),
  onPrimaryContainer = EmeraldGlow,
  secondary = CyanNeon,
  onSecondary = CyberBlack,
  secondaryContainer = Color(0xFF164E63),
  onSecondaryContainer = CyanGlow,
  tertiary = PurpleNeon,
  onTertiary = Color.White,
  tertiaryContainer = Color(0xFF3B0764),
  onTertiaryContainer = PurpleGlow,
  background = CyberBlack,
  onBackground = TextPrimary,
  surface = DarkSurface,
  onSurface = TextPrimary,
  surfaceVariant = CyberCard,
  onSurfaceVariant = TextSecondary,
  outline = CyberCardBorder,
  error = ErrorRed,
  onError = Color.White
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = PruconDarkColorScheme,
    typography = Typography,
    content = content
  )
}

