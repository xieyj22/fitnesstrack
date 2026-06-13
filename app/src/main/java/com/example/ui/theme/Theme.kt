package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeType {
    SAGE_ORGANIC,
    COSMIC_OBSIDIAN,
    AURA_CORAL,
    SILENT_LAVENDER
}

// 1. Sage Organic Theme (Light & Dark)
private val SageLightColorScheme = lightColorScheme(
    primary = Color(0xFF386A20),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDBE6D3),
    onPrimaryContainer = Color(0xFF131F0E),
    secondary = Color(0xFF43493E),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF2F2EB),
    onSecondaryContainer = Color(0xFF1A1C18),
    background = Color(0xFFFCFDF6),
    onBackground = Color(0xFF1A1C18),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1A1C18),
    surfaceVariant = Color(0xFFE1E3D3),
    onSurfaceVariant = Color(0xFF43493E),
    outline = Color(0xFF74796D),
    outlineVariant = Color(0xFFC4C8BB)
)

private val SageDarkColorScheme = darkColorScheme(
    primary = Color(0xFF386A20),
    secondary = Color(0xFF43493E),
    tertiary = Color(0xFFDBE6D3),
    background = Color(0xFF131F0E),
    surface = Color(0xFF1A1C18),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFDBE6D3),
    onSurface = Color(0xFFDBE6D3)
)

// 2. Cosmic Obsidian (Dark Mode Void with glowing Cyan & Amethyst accents)
private val CosmicColorScheme = darkColorScheme(
    primary = Color(0xFF00E5FF),          // Ambient Neon Cyan
    onPrimary = Color(0xFF00363D),
    primaryContainer = Color(0xFF004E57),
    onPrimaryContainer = Color(0xFFB2F5FF),
    secondary = Color(0xFF9D4EDD),        // Cosmic Amethyst
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF240046),
    onSecondaryContainer = Color(0xFFF3E5F5),
    background = Color(0xFF08090C),       // Space Void
    onBackground = Color(0xFFE2E2E6),
    surface = Color(0xFF12141C),          // Deep Glass Panel
    onSurface = Color(0xFFE2E2E6),
    surfaceVariant = Color(0xFF20232E),
    onSurfaceVariant = Color(0xFFC4C6D0),
    outline = Color(0xFF8E9099),
    outlineVariant = Color(0xFF44474F)
)

// 3. Aura Coral Theme (Warm Linen & Vivid Terracotta)
private val CoralColorScheme = lightColorScheme(
    primary = Color(0xFFE0533C),          // Vivid Terracotta
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFEBE7),
    onPrimaryContainer = Color(0xFF5F0C00),
    secondary = Color(0xFFE6A15C),        // Sunset Peach
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFF8F0),
    onSecondaryContainer = Color(0xFF4D2C00),
    background = Color(0xFFFFFBF9),       // Soft Warm Linen
    onBackground = Color(0xFF2B2523),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF2B2523),
    surfaceVariant = Color(0xFFF7ECE9),
    onSurfaceVariant = Color(0xFF5C514E),
    outline = Color(0xFF8F7E79),
    outlineVariant = Color(0xFFDECBC6)
)

// 4. Silent Lavender Theme (Healing Lilac & Mineral Stone)
private val LavenderColorScheme = lightColorScheme(
    primary = Color(0xFF6F52A2),          // Peaceful Lavender
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEDE8F5),
    onPrimaryContainer = Color(0xFF28104E),
    secondary = Color(0xFF5A687A),        // Slate Blue Gray
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF4F6F9),
    onSecondaryContainer = Color(0xFF1B2430),
    background = Color(0xFFFAF9FC),       // Soft Mineral white
    onBackground = Color(0xFF1E1A24),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF1E1A24),
    surfaceVariant = Color(0xFFECE6F2),
    onSurfaceVariant = Color(0xFF4E4757),
    outline = Color(0xFF7F748D),
    outlineVariant = Color(0xFFDCD6E3)
)

@Composable
fun MyApplicationTheme(
    themeType: ThemeType = ThemeType.SAGE_ORGANIC,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = when (themeType) {
        ThemeType.SAGE_ORGANIC -> if (darkTheme) SageDarkColorScheme else SageLightColorScheme
        ThemeType.COSMIC_OBSIDIAN -> CosmicColorScheme
        ThemeType.AURA_CORAL -> CoralColorScheme
        ThemeType.SILENT_LAVENDER -> LavenderColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
