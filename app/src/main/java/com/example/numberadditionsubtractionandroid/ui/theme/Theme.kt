package com.example.numberadditionsubtractionandroid.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val KidsColorScheme = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = SkyBlue.copy(alpha = 0.2f),
    secondary = SecondaryColor,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A1A1A),
    error = WrongRed
)

@Composable
fun NumberAdditionSubtractionAndroidTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KidsColorScheme,
        typography = Typography,
        content = content
    )
}