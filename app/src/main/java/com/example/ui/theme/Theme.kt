package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = JavaGoldPrimary,
    secondary = JavaGreenPrimary,
    tertiary = JavaClay,
    background = JavaneseDarkBackground,
    surface = JavaneseDarkSurface,
    onPrimary = JavaneseLightBackground,
    onSecondary = JavaneseLightBackground,
    onBackground = JavaneseLightBackground,
    onSurface = JavaneseLightBackground
  )

private val LightColorScheme =
  lightColorScheme(
    primary = JavaGreenPrimary,
    secondary = JavaGoldPrimary,
    tertiary = JavaTerracotta,
    background = JavaneseLightBackground,
    surface = JavaneseLightSurface,
    onPrimary = JavaneseLightSurface,
    onSecondary = JavaneseBatikText,
    onBackground = JavaneseBatikText,
    onSurface = JavaneseBatikText
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is set to false to keep our immersive Javanese brand styling
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
