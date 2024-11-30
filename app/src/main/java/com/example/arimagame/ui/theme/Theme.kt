package com.example.arimagame.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

val LightGray = Color(0xFFEEEEEE)
val DarkGray = Color(0xFFAAAAAA)
val Red = Color(0xFFFF0000)

private val DarkColorScheme = darkColorScheme(
    primary = DarkGray,
    secondary =LightGray
)

private val LightColorScheme = lightColorScheme(
    primary = DarkGray,
    secondary =LightGray
)

@Composable
fun ArimaGameTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}