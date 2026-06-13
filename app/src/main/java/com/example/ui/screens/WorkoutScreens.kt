package com.example.ui.screens

import android.app.DatePickerDialog
import androidx.compose.animation.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.WorkoutLog
import com.example.data.CustomExercise
import com.example.ui.viewmodel.WorkoutViewModel
import com.example.ui.translation.LanguageManager
import com.example.ui.translation.LanguageType
import com.example.ui.theme.ThemeType
import java.util.*

@Composable
fun AppNavigationWrapper(viewModel: WorkoutViewModel) {
    var currentTab by remember { mutableStateOf(0) } // 0: Today, 1: Stats, 2: Library, 3: Settings
    val language by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_nav_bar")
            ) {
                NavigationBarItem(
                    selected = currentTab == 0,
                    onClick = { currentTab = 0 },
                    icon = { Icon(Icons.Default.Home, contentDescription = LanguageManager.getString("tab_today", language)) },
                    label = { Text(LanguageManager.getString("tab_today", language)) },
                    modifier = Modifier.testTag("nav_tab_today")
                )
                NavigationBarItem(
                    selected = currentTab == 1,
                    onClick = { currentTab = 1 },
                    icon = { Icon(Icons.Default.DateRange, contentDescription = LanguageManager.getString("tab_stats", language)) },
                    label = { Text(LanguageManager.getString("tab_stats", language)) },
                    modifier = Modifier.testTag("nav_tab_stats")
                )
                NavigationBarItem(
                    selected = currentTab == 2,
                    onClick = { currentTab = 2 },
                    icon = { Icon(Icons.Default.List, contentDescription = LanguageManager.getString("tab_library", language)) },
                    label = { Text(LanguageManager.getString("tab_library", language)) },
                    modifier = Modifier.testTag("nav_tab_library")
                )
                NavigationBarItem(
                    selected = currentTab == 3,
                    onClick = { currentTab = 3 },
                    icon = { Icon(Icons.Default.Settings, contentDescription = LanguageManager.getString("tab_settings", language)) },
                    label = { Text(LanguageManager.getString("tab_settings", language)) },
                    modifier = Modifier.testTag("nav_tab_settings")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTab) {
                0 -> TodayScreen(viewModel = viewModel)
                1 -> StatsScreen(viewModel = viewModel)
                2 -> ExerciseLibraryScreen(viewModel = viewModel)
                3 -> SettingsScreen(viewModel = viewModel)
            }
        }
    }

    // Modal Add/Edit Dialog
    val showDialog by viewModel.showFormDialog.collectAsStateWithLifecycle()
    if (showDialog) {
        WorkoutFormDialog(viewModel = viewModel)
    }
}

@Composable
fun SettingsScreen(viewModel: WorkoutViewModel) {
    val context = LocalContext.current
    val currentTheme by viewModel.selectedTheme.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Custom Header
        Column(modifier = Modifier.padding(bottom = 16.dp, top = 12.dp)) {
            Text(
                text = LanguageManager.getString("settings_title", currentLanguage),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = LanguageManager.getString("settings_footer", currentLanguage),
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            // Theme selection card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.getString("settings_theme_label", currentLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Theme Option Rows
                        val themeOptions = listOf(
                            ThemeType.SAGE_ORGANIC to (LanguageManager.getString("theme_sage", currentLanguage) to Color(0xFF386A20)),
                            ThemeType.COSMIC_OBSIDIAN to (LanguageManager.getString("theme_cosmic", currentLanguage) to Color(0xFF00E5FF)),
                            ThemeType.AURA_CORAL to (LanguageManager.getString("theme_coral", currentLanguage) to Color(0xFFE0533C)),
                            ThemeType.SILENT_LAVENDER to (LanguageManager.getString("theme_lavender", currentLanguage) to Color(0xFF6F52A2))
                        )

                        themeOptions.forEach { (type, optionInfo) ->
                            val (label, primColor) = optionInfo
                            val isSelected = currentTheme == type

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .clickable { viewModel.selectTheme(type, context) }
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent)
                                    .padding(vertical = 10.dp, horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Mini Theme circle
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .background(primColor, CircleShape)
                                        .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = label,
                                    fontSize = 13.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Language Selection Card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.getString("settings_language_label", currentLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        val languageOptions = listOf(
                            LanguageType.CHINESE to "简体中文",
                            LanguageType.ENGLISH to "English",
                            LanguageType.JAPANESE to "日本語"
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            languageOptions.forEach { (type, label) ->
                                val isSelected = currentLanguage == type
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.selectLanguage(type, context) },
                                    label = { Text(label, fontSize = 12.sp) },
                                    modifier = Modifier.weight(1f).testTag("lang_chip_${type.name}")
                                )
                            }
                        }
                    }
                }
            }

            // About us card
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = LanguageManager.getString("settings_about_label", currentLanguage),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = LanguageManager.getString("settings_about_text", currentLanguage),
                            fontSize = 12.sp,
                            lineHeight = 18.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TodayScreen(viewModel: WorkoutViewModel) {
    val allLogs by viewModel.allWorkouts.collectAsStateWithLifecycle()
    val todayStartMillis = getTodayStartMillis()
    val todayEndMillis = todayStartMillis + 86400000L - 1L
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    // Filter today's training
    val todayLogs = remember(allLogs) {
        allLogs.filter { it.dateMillis in todayStartMillis..todayEndMillis }
    }

    // Dynamic aggregates
    val todayCardioMin = remember(todayLogs) {
        todayLogs.filter { it.category == "CARDIO" }.sumOf { it.durationMinutes }
    }
    val todayStrengthSets = remember(todayLogs) {
        todayLogs.filter { it.category == "STRENGTH" }.sumOf { it.sets }
    }
    val todayCustomMin = remember(todayLogs) {
        todayLogs.filter { it.category == "CUSTOM" || it.isCustom }.sumOf { it.durationMinutes }
    }
    val todayCalories = remember(todayLogs) {
        todayLogs.sumOf { it.caloriesBurned }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.openAddDialog() },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .testTag("add_workout_fab")
                    .padding(bottom = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = LanguageManager.getString("dialog_title_add", currentLanguage))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        val formattedDate = WorkoutViewModel.formatDate(System.currentTimeMillis()) + " " + getLocalizedDayOfWeek(currentLanguage)
                        Text(
                            text = formattedDate.uppercase(Locale.getDefault()),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = LanguageManager.getString("welcome_user", currentLanguage),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            letterSpacing = (-0.5).sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User Profile Avatar",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // Summary row cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SummaryCard(
                        title = LanguageManager.getString("stat_cardio", currentLanguage),
                        value = "$todayCardioMin",
                        unit = LanguageManager.getString("unit_minutes", currentLanguage),
                        icon = Icons.Default.PlayArrow,
                        color = MaterialTheme.colorScheme.primary, // Sage green
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = LanguageManager.getString("stat_strength", currentLanguage),
                        value = "$todayStrengthSets",
                        unit = LanguageManager.getString("unit_sets", currentLanguage),
                        icon = Icons.Default.Favorite,
                        color = Color(0xFF8D5524), // complementary clay/bronze color
                        modifier = Modifier.weight(1f)
                    )
                    SummaryCard(
                        title = LanguageManager.getString("stat_custom", currentLanguage),
                        value = "$todayCustomMin",
                        unit = LanguageManager.getString("unit_minutes", currentLanguage),
                        icon = Icons.Default.Star,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                SummaryCardFull(
                    title = LanguageManager.getString("stat_calories", currentLanguage),
                    value = "$todayCalories",
                    unit = LanguageManager.getString("unit_kcal", currentLanguage),
                    icon = Icons.Default.Star,
                    color = Color(0xFFFF5722) // Active thermal color
                )
            }

            // Workout logs title
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val logsCountText = when (currentLanguage) {
                        LanguageType.ENGLISH -> "Today's Activities (${todayLogs.size})"
                        LanguageType.JAPANESE -> "今日の運動種目 (${todayLogs.size})"
                        else -> "今日运动项目 (${todayLogs.size})"
                    }
                    Text(
                        text = logsCountText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (todayLogs.isNotEmpty()) {
                        val slideHintText = when (currentLanguage) {
                            LanguageType.ENGLISH -> "Swipe card or click to edit"
                            LanguageType.JAPANESE -> "スワイプまたはタップして編集"
                            else -> "滑动卡片或点击编辑"
                        }
                        Text(
                            text = slideHintText,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // List of today workouts
            if (todayLogs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "无运动",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = LanguageManager.getString("empty_logs_today", currentLanguage),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        val startLogHint = when (currentLanguage) {
                            LanguageType.ENGLISH -> "Tap the '+' below to start recording!"
                            LanguageType.JAPANESE -> "下部の '+' をタップして記録を始めましょう！"
                            else -> "点击下方的 '+' 号开始记录一次运动吧！"
                        }
                        Text(
                            text = startLogHint,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }
                }
            } else {
                items(todayLogs, key = { it.id }) { log ->
                    WorkoutItemRow(
                        log = log,
                        language = currentLanguage,
                        onEditClick = { viewModel.openEditDialog(log) },
                        onDeleteClick = { viewModel.deleteWorkout(log) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                .padding(18.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                Text(text = title.uppercase(), fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = unit,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun SummaryCardFull(
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    color: Color
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                .padding(18.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), shape = RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
                Column {
                    Text(text = title.uppercase(), fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = value,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = unit,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
        }
    }
}

@Composable
fun WorkoutItemRow(
    log: WorkoutLog,
    language: LanguageType,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val isCardio = log.category == "CARDIO"
    val isCustom = log.category == "CUSTOM" || log.isCustom
    val themeColor = if (isCustom) {
        MaterialTheme.colorScheme.secondary
    } else if (isCardio) {
        MaterialTheme.colorScheme.primary
    } else {
        Color(0xFF8D5524)
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() }
            .testTag("workout_item_${log.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Badge
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(themeColor.copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCustom) Icons.Default.Star else if (isCardio) Icons.Default.PlayArrow else Icons.Default.Favorite,
                    contentDescription = null,
                    tint = themeColor
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Information details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = LanguageManager.getExerciseName(log.exerciseName, language),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(themeColor.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        val catLabelText = if (isCustom) {
                            LanguageManager.getString("cat_custom", language)
                        } else if (isCardio) {
                            LanguageManager.getString("cat_cardio", language)
                        } else {
                            LanguageManager.getString("cat_strength", language)
                        }
                        Text(
                            text = catLabelText,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = themeColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))

                // Stats text
                val statsText = if (isCustom) {
                    "${log.customValue} ${log.customUnit}"
                } else if (isCardio) {
                    val unitMins = if (log.customUnit.isNotEmpty() && log.customUnit != "分钟" && log.customUnit != "mins" && log.customUnit != "分") {
                        log.customUnit
                    } else {
                        LanguageManager.getString("unit_minutes", language)
                    }
                    val unitKm = LanguageManager.getString("unit_distance", language)
                    val distStr = if (log.distanceKm > 0.0) "${log.distanceKm} $unitKm | " else ""
                    "$distStr${log.durationMinutes} $unitMins"
                } else {
                    val unitSets = LanguageManager.getString("unit_sets", language)
                    val unitReps = LanguageManager.getString("unit_reps", language)
                    val unitWeight = if (log.customUnit.isNotEmpty() && log.customUnit != "分钟" && log.customUnit != "mins" && log.customUnit != "分") {
                        log.customUnit
                    } else {
                        LanguageManager.getString("unit_weight", language)
                    }
                    "${log.sets} $unitSets | ${log.reps} $unitReps | ${log.weightKg} $unitWeight"
                }
                Text(
                    text = statsText,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (log.caloriesBurned > 0) {
                    val labelKcal = LanguageManager.getString("unit_kcal", language)
                    val burnLabelText = when (language) {
                        LanguageType.ENGLISH -> "🔥 Burneded: ${log.caloriesBurned} $labelKcal"
                        LanguageType.JAPANESE -> "🔥 消費: ${log.caloriesBurned} $labelKcal"
                        else -> "🔥 消耗: ${log.caloriesBurned} $labelKcal"
                    }
                    Text(
                        text = burnLabelText,
                        fontSize = 11.sp,
                        color = Color(0xFFFF5722),
                        fontWeight = FontWeight.Medium
                    )
                }

                if (log.notes.isNotEmpty()) {
                    Text(
                        text = "📝 ${log.notes}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row {
                IconButton(
                    onClick = onEditClick,
                    modifier = Modifier.testTag("workout_edit_btn_${log.id}")
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "编辑",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.testTag("workout_delete_btn_${log.id}")
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "删除",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun WorkoutFormDialog(viewModel: WorkoutViewModel) {
    val context = LocalContext.current
    val isEditing by viewModel.formIsEditing.collectAsStateWithLifecycle()
    val category by viewModel.formCategory.collectAsStateWithLifecycle()
    val selectedExercise by viewModel.formExerciseName.collectAsStateWithLifecycle()
    val dateMillis by viewModel.formDateMillis.collectAsStateWithLifecycle()
    val durationMinutes by viewModel.formDurationMinutes.collectAsStateWithLifecycle()
    val distanceKm by viewModel.formDistanceKm.collectAsStateWithLifecycle()
    val weightKg by viewModel.formWeightKg.collectAsStateWithLifecycle()
    val reps by viewModel.formReps.collectAsStateWithLifecycle()
    val sets by viewModel.formSets.collectAsStateWithLifecycle()
    val caloriesBurned by viewModel.formCaloriesBurned.collectAsStateWithLifecycle()
    val notes by viewModel.formNotes.collectAsStateWithLifecycle()
    val errorMsg by viewModel.formError.collectAsStateWithLifecycle()
    val customUnit by viewModel.formCustomUnit.collectAsStateWithLifecycle()
    val customValue by viewModel.formCustomValue.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    val cardioExercisesCount by viewModel.allCardioExercises.collectAsStateWithLifecycle()
    val strengthExercisesCount by viewModel.allStrengthExercises.collectAsStateWithLifecycle()
    val customExercisesList by viewModel.customExercisesList.collectAsStateWithLifecycle()
    val selectedCustomExercise = customExercisesList.find { it.name == selectedExercise }
    val selectedExerciseUnit = selectedCustomExercise?.unit

    var customExerciseEntered by remember { mutableStateOf("") }
    var useCustomExercise by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = { viewModel.showFormDialog.value = false }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("workout_form_dialog"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    val dialogHeading = LanguageManager.getString(
                        if (isEditing) "dialog_title_edit" else "dialog_title_add",
                        currentLanguage
                    )
                    Text(
                        text = dialogHeading,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }

                // Category Tabs
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.onCategoryChange("CARDIO") }
                                .background(if (category == "CARDIO") MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 8.dp)
                                .testTag("form_tab_cardio"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LanguageManager.getString("cat_cardio", currentLanguage),
                                color = if (category == "CARDIO") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.onCategoryChange("STRENGTH") }
                                .background(if (category == "STRENGTH") MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 8.dp)
                                .testTag("form_tab_strength"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LanguageManager.getString("cat_strength", currentLanguage),
                                color = if (category == "STRENGTH") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.onCategoryChange("CUSTOM") }
                                .background(if (category == "CUSTOM") MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 8.dp)
                                .testTag("form_tab_custom"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                LanguageManager.getString("cat_custom", currentLanguage),
                                color = if (category == "CUSTOM") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Date Picker trigger
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val calendar = Calendar.getInstance().apply { timeInMillis = dateMillis }
                                DatePickerDialog(
                                    context,
                                    { _, year, month, dayOfMonth ->
                                        val cal = Calendar.getInstance().apply {
                                            set(Calendar.YEAR, year)
                                            set(Calendar.MONTH, month)
                                            set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                            set(Calendar.HOUR_OF_DAY, 8) // Local mid-day to avoid TZ shifts
                                            set(Calendar.MINUTE, 0)
                                            set(Calendar.SECOND, 0)
                                            set(Calendar.MILLISECOND, 0)
                                        }
                                        viewModel.formDateMillis.value = cal.timeInMillis
                                    },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ).show()
                            }
                            .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                            .testTag("form_date_picker"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            val dateLabel = when (currentLanguage) {
                                LanguageType.ENGLISH -> "Workout Date"
                                LanguageType.JAPANESE -> "トレーニング日"
                                else -> "训练日期"
                            }
                            Text(dateLabel, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        }
                        Text(
                            text = WorkoutViewModel.formatDate(dateMillis),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 14.sp
                        )
                    }
                }

                // Exercise Selection
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (category != "CUSTOM") {
                            Text(
                                LanguageManager.getString("field_exercise_name", currentLanguage),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            if (!useCustomExercise) {
                                // Standard List Options Row
                                val options = if (category == "CARDIO") cardioExercisesCount else strengthExercisesCount
                                FlowRow( // Clean flow wrapping of chips
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    options.forEach { option ->
                                        val isSelected = selectedExercise == option
                                        FilterChip(
                                            selected = isSelected,
                                            onClick = {
                                                viewModel.formExerciseName.value = option
                                                val foundUnit = customExercisesList.find { it.name == option }?.unit
                                                viewModel.formCustomUnit.value = foundUnit ?: ""
                                            },
                                            label = { Text(LanguageManager.getExerciseName(option, currentLanguage)) },
                                            modifier = Modifier.testTag("chip_$option")
                                        )
                                    }
                                    // Addition custom click
                                    val customChipLabel = when (currentLanguage) {
                                        LanguageType.ENGLISH -> "[Custom]"
                                        LanguageType.JAPANESE -> "[カスタム]"
                                        else -> "[自定义]"
                                    }
                                    FilterChip(
                                        selected = false,
                                        onClick = {
                                            useCustomExercise = true
                                            viewModel.formExerciseName.value = ""
                                        },
                                        label = { Text(customChipLabel) },
                                        leadingIcon = { Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp)) }
                                    )
                                }
                            } else {
                                val customProjectLabel = when (currentLanguage) {
                                    LanguageType.ENGLISH -> "Custom Project Name"
                                    LanguageType.JAPANESE -> "カスタム種目名"
                                    else -> "自定义项目名称"
                                }
                                val backPresetDesc = when (currentLanguage) {
                                    LanguageType.ENGLISH -> "Back to Presets"
                                    LanguageType.JAPANESE -> "プリセットに戻る"
                                    else -> "返回预设"
                                }
                                OutlinedTextField(
                                    value = customExerciseEntered,
                                    onValueChange = {
                                        customExerciseEntered = it
                                        viewModel.formExerciseName.value = it
                                    },
                                    label = { Text(customProjectLabel) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Default.ArrowBack,
                                            contentDescription = backPresetDesc,
                                            modifier = Modifier
                                                .clickable {
                                                    useCustomExercise = false
                                                    viewModel.formExerciseName.value = if (category == "CARDIO") viewModel.cardioExercises.first() else viewModel.strengthExercises.first()
                                                }
                                                .padding(8.dp)
                                        )
                                    },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("custom_exercise_input")
                                )
                            }
                        } else {
                            // Direct Custom Exercise Name Field
                            OutlinedTextField(
                                value = selectedExercise,
                                onValueChange = {
                                    viewModel.formExerciseName.value = it
                                },
                                label = { Text(LanguageManager.getString("field_custom_exercise_hint", currentLanguage)) },
                                singleLine = true,
                                placeholder = { Text(LanguageManager.getString("field_custom_exercise_placeholder", currentLanguage)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("custom_exercise_name_input")
                            )
                        }
                    }
                }

                // Conditional Fields
                if (category == "CARDIO") {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = durationMinutes,
                                onValueChange = { viewModel.formDurationMinutes.value = it.filter { c -> c.isDigit() } },
                                label = {
                                    val finalLabel = if (selectedExerciseUnit != null && selectedExerciseUnit.isNotEmpty()) {
                                        val baseLabel = if (currentLanguage == LanguageType.CHINESE) "运动时间" else if (currentLanguage == LanguageType.JAPANESE) "運動時間" else "Duration"
                                        "$baseLabel ($selectedExerciseUnit) *"
                                    } else {
                                        LanguageManager.getString("field_duration_req", currentLanguage)
                                    }
                                    Text(finalLabel)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                trailingIcon = { Icon(Icons.Default.PlayArrow, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_input_duration")
                            )

                            OutlinedTextField(
                                value = distanceKm,
                                onValueChange = { viewModel.formDistanceKm.value = it },
                                label = { Text(LanguageManager.getString("field_distance_opt", currentLanguage)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                trailingIcon = { Icon(Icons.Default.Info, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_input_distance")
                            )
                        }
                    }
                } else if (category == "STRENGTH") {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = sets,
                                    onValueChange = { viewModel.formSets.value = it.filter { c -> c.isDigit() } },
                                    label = { Text(LanguageManager.getString("field_sets", currentLanguage)) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("form_input_sets")
                                )
                                OutlinedTextField(
                                    value = reps,
                                    onValueChange = { viewModel.formReps.value = it.filter { c -> c.isDigit() } },
                                    label = { Text(LanguageManager.getString("field_reps", currentLanguage)) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("form_input_reps")
                                )
                            }

                            OutlinedTextField(
                                value = weightKg,
                                onValueChange = { viewModel.formWeightKg.value = it },
                                label = {
                                    val finalLabel = if (selectedExerciseUnit != null && selectedExerciseUnit.isNotEmpty()) {
                                        val baseLabel = if (currentLanguage == LanguageType.CHINESE) "重量" else if (currentLanguage == LanguageType.JAPANESE) "重量" else "Weight"
                                        "$baseLabel ($selectedExerciseUnit)"
                                    } else {
                                        LanguageManager.getString("field_weight", currentLanguage)
                                    }
                                    Text(finalLabel)
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_input_weight")
                            )
                        }
                    }
                } else if (category == "CUSTOM") {
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            OutlinedTextField(
                                value = customValue,
                                onValueChange = { viewModel.formCustomValue.value = it.filter { c -> c.isDigit() || c == '.' } },
                                label = { Text(LanguageManager.getString("field_custom_val_req", currentLanguage)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                singleLine = true,
                                placeholder = { Text(LanguageManager.getString("field_custom_val_placeholder", currentLanguage)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("form_input_custom_value")
                            )

                            Text(
                                text = LanguageManager.getString("field_custom_unit_req", currentLanguage),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            val unitsMap = mapOf(
                                "分钟" to when (currentLanguage) {
                                    LanguageType.ENGLISH -> "mins"
                                    LanguageType.JAPANESE -> "分"
                                    else -> "分钟"
                                },
                                "小时" to when (currentLanguage) {
                                    LanguageType.ENGLISH -> "hours"
                                    LanguageType.JAPANESE -> "時間"
                                    else -> "小时"
                                }
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                unitsMap.forEach { (key, displayValue) ->
                                    val isSelected = customUnit == key || customUnit == displayValue
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { viewModel.formCustomUnit.value = displayValue },
                                        label = { Text(displayValue) },
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("custom_unit_chip_$key")
                                    )
                                }
                            }
                        }
                    }
                }

                // Estimated calories and notes (Joint fields)
                item {
                    OutlinedTextField(
                        value = caloriesBurned,
                        onValueChange = { viewModel.formCaloriesBurned.value = it.filter { c -> c.isDigit() } },
                        label = { Text(LanguageManager.getString("field_calories", currentLanguage)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_input_calories")
                    )
                }

                item {
                    OutlinedTextField(
                        value = notes,
                        onValueChange = { viewModel.formNotes.value = it },
                        label = { Text(LanguageManager.getString("field_notes", currentLanguage)) },
                        maxLines = 3,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("form_input_notes")
                    )
                }

                // Error message displays
                if (errorMsg != null) {
                    item {
                        val transError = when (errorMsg) {
                            "有氧运动请填写有效的时长" -> LanguageManager.getString("err_duration", currentLanguage)
                            "力量训练请填写有效的组数" -> LanguageManager.getString("err_sets", currentLanguage)
                            "自定义运动请填写有效的数值" -> LanguageManager.getString("err_custom", currentLanguage)
                            "请填写运动项目名称" -> LanguageManager.getString("err_exercise_name", currentLanguage)
                            else -> errorMsg!!
                        }
                        Text(
                            text = "⚠️ $transError",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Actions row
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.showFormDialog.value = false },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("form_cancel_button")
                        ) {
                            Text(LanguageManager.getString("btn_cancel", currentLanguage))
                        }
                        Button(
                            onClick = { viewModel.saveWorkout() },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("form_submit_button")
                        ) {
                            Text(
                                LanguageManager.getString(
                                    if (isEditing) "btn_update" else "dialog_title_add",
                                    currentLanguage
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatsScreen(viewModel: WorkoutViewModel) {
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val selectedMonth by viewModel.selectedMonth.collectAsStateWithLifecycle()
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()

    var isMonthlyAnalysis by remember { mutableStateOf(true) } // true for monthly, false for yearly

    val monthlyLogs by viewModel.monthlyWorkouts.collectAsStateWithLifecycle()
    val yearlyLogs by viewModel.yearlyWorkouts.collectAsStateWithLifecycle()

    val logs = if (isMonthlyAnalysis) monthlyLogs else yearlyLogs

    // Aggregate values
    val totalWorkouts = logs.size
    val totalCalories = logs.sumOf { it.caloriesBurned }
    val totalCardioMins = logs.filter { it.category == "CARDIO" }.sumOf { it.durationMinutes }
    val totalStrengthSets = logs.filter { it.category == "STRENGTH" }.sumOf { it.sets }
    val totalCustomMins = logs.filter { it.category == "CUSTOM" || it.isCustom }.sumOf { it.durationMinutes }

    // Unique training days
    val activeDays = remember(logs) {
        logs.map {
            WorkoutViewModel.getDayFromMillis(it.dateMillis) to WorkoutViewModel.getMonthFromMillis(it.dateMillis)
        }.distinct().size
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Stats Title
        item {
            Spacer(modifier = Modifier.height(16.dp))
            val screenTitle = when(currentLanguage) {
                LanguageType.ENGLISH -> "Activity Analysis"
                LanguageType.JAPANESE -> "運動分析ダッシュボード"
                else -> "训练分析看板"
            }
            Text(
                text = screenTitle,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Toggle Switch Option Row
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                val tabMonthly = when(currentLanguage) {
                    LanguageType.ENGLISH -> "Monthly Stats"
                    LanguageType.JAPANESE -> "月間統計"
                    else -> "月度统计记录"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isMonthlyAnalysis = true }
                        .background(if (isMonthlyAnalysis) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .padding(vertical = 10.dp)
                        .testTag("stats_tab_monthly"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        tabMonthly,
                        color = if (isMonthlyAnalysis) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                val tabYearly = when(currentLanguage) {
                    LanguageType.ENGLISH -> "Yearly Stats"
                    LanguageType.JAPANESE -> "年間統計"
                    else -> "年度统计记录"
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { isMonthlyAnalysis = false }
                        .background(if (!isMonthlyAnalysis) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .padding(vertical = 10.dp)
                        .testTag("stats_tab_yearly"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        tabYearly,
                        color = if (!isMonthlyAnalysis) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Selected Period Navigator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (isMonthlyAnalysis) viewModel.decrementMonth() else viewModel.decrementYear()
                        },
                        modifier = Modifier.testTag("prev_period_btn")
                    ) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous")
                    }

                    val periodText = if (isMonthlyAnalysis) {
                        when (currentLanguage) {
                            LanguageType.ENGLISH -> {
                                val monthName = when (selectedMonth) {
                                    1 -> "Jan"; 2 -> "Feb"; 3 -> "Mar"; 4 -> "Apr"; 5 -> "May"; 6 -> "Jun"
                                    7 -> "Jul"; 8 -> "Aug"; 9 -> "Sep"; 10 -> "Oct"; 11 -> "Nov"; 12 -> "Dec"
                                    else -> ""
                                }
                                "$monthName $selectedYear"
                            }
                            LanguageType.JAPANESE -> "$selectedYear 年 $selectedMonth 月"
                            else -> "$selectedYear 年 $selectedMonth 月"
                        }
                    } else {
                        when (currentLanguage) {
                            LanguageType.ENGLISH -> "$selectedYear"
                            else -> "$selectedYear 年"
                        }
                    }
                    Text(
                        text = periodText,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    IconButton(
                        onClick = {
                            if (isMonthlyAnalysis) viewModel.incrementMonth() else viewModel.incrementYear()
                        },
                        modifier = Modifier.testTag("next_period_btn")
                    ) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                    }
                }
            }
        }

        // Statistical KPI Grid Cards
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                val kpiActiveDays = when(currentLanguage) {
                    LanguageType.ENGLISH -> "Active Days"
                    LanguageType.JAPANESE -> "活動日数"
                    else -> "累计运动天数"
                }
                val cardUnitDays = when(currentLanguage) {
                    LanguageType.ENGLISH -> "days"
                    LanguageType.JAPANESE -> "日"
                    else -> "天"
                }
                val kpiWorkoutsCount = when(currentLanguage) {
                    LanguageType.ENGLISH -> "Workouts Count"
                    LanguageType.JAPANESE -> "運動回数"
                    else -> "参与运动次数"
                }
                val cardUnitTimes = when(currentLanguage) {
                    LanguageType.ENGLISH -> "times"
                    LanguageType.JAPANESE -> "回"
                    else -> "次"
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    KpiStatCard(
                        title = kpiActiveDays,
                        value = "$activeDays",
                        unit = cardUnitDays,
                        modifier = Modifier.weight(1f)
                    )
                    KpiStatCard(
                        title = kpiWorkoutsCount,
                        value = "$totalWorkouts",
                        unit = cardUnitTimes,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    KpiStatCard(
                        title = LanguageManager.getString("stat_cardio", currentLanguage),
                        value = "$totalCardioMins",
                        unit = LanguageManager.getString("unit_minutes", currentLanguage),
                        modifier = Modifier.weight(1f)
                    )
                    KpiStatCard(
                        title = LanguageManager.getString("stat_strength", currentLanguage),
                        value = "$totalStrengthSets",
                        unit = LanguageManager.getString("unit_sets", currentLanguage),
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    KpiStatCard(
                        title = LanguageManager.getString("stat_custom", currentLanguage),
                        value = "$totalCustomMins",
                        unit = LanguageManager.getString("unit_minutes", currentLanguage),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Calories burned indicator row
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🔥", fontSize = 24.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                LanguageManager.getString("stats_calories_burned", currentLanguage),
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )
                            val labelKcal = LanguageManager.getString("unit_kcal", currentLanguage)
                            Text(
                                "$totalCalories $labelKcal",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF5722)
                            )
                        }
                    }
                }
            }
        }

        // Custom Visual Graph Trend Section
        item {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    val trendTitle = if (isMonthlyAnalysis) {
                        when (currentLanguage) {
                            LanguageType.ENGLISH -> "Daily Trend (Workouts / Day)"
                            LanguageType.JAPANESE -> "日次アクティビティ分布 (回数/日)"
                            else -> "每日活动趋势分布 (次数 / 天)"
                        }
                    } else {
                        when (currentLanguage) {
                            LanguageType.ENGLISH -> "Monthly Trend (Workouts / Month)"
                            LanguageType.JAPANESE -> "月次アクティビティ分布 (回数/月)"
                            else -> "每月活动分布趋势 (次数 / 月)"
                        }
                    }
                    Text(
                        text = trendTitle,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (isMonthlyAnalysis) {
                        val calendarInstance = Calendar.getInstance().apply {
                            set(Calendar.YEAR, selectedYear)
                            set(Calendar.MONTH, selectedMonth - 1)
                        }
                        val maxDay = calendarInstance.getActualMaximum(Calendar.DAY_OF_MONTH)

                        // Key: Day of month, Value: Count of workouts on that day
                        val dayMap = remember(monthlyLogs) {
                            val map = mutableMapOf<Int, Int>()
                            monthlyLogs.forEach { log ->
                                val day = WorkoutViewModel.getDayFromMillis(log.dateMillis)
                                map[day] = (map[day] ?: 0) + 1
                            }
                            map
                        }

                        MonthlyTrendCanvas(
                            maxDay = maxDay,
                            dailyCounts = dayMap,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    } else {
                        // Key: month (1..12), value: count
                        val monthMap = remember(yearlyLogs) {
                            val map = mutableMapOf<Int, Int>()
                            yearlyLogs.forEach { log ->
                                val m = WorkoutViewModel.getMonthFromMillis(log.dateMillis)
                                map[m] = (map[m] ?: 0) + 1
                            }
                            map
                        }

                        YearlyTrendCanvas(
                            monthlyCounts = monthMap,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                        )
                    }
                }
            }
        }

        // Detailed List logs title
        item {
            val listTitleText = when(currentLanguage) {
                LanguageType.ENGLISH -> "Workout History (${logs.size})"
                LanguageType.JAPANESE -> "運動履歴ログ (${logs.size})"
                else -> "训练轨迹日志 (${logs.size})"
            }
            Text(
                text = listTitleText,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (logs.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        LanguageManager.getString("empty_logs_stats", currentLanguage),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            items(logs, key = { it.id }) { logItem ->
                HistoricalLogCardItem(
                    log = logItem,
                    language = currentLanguage,
                    onEditClick = { viewModel.openEditDialog(logItem) },
                    onDeleteClick = { viewModel.deleteWorkout(logItem) }
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun KpiStatCard(
    title: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                .padding(14.dp)
        ) {
            Text(
                text = title,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Light,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = unit,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }
        }
    }
}

@Composable
fun MonthlyTrendCanvas(
    maxDay: Int,
    dailyCounts: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val barColor = MaterialTheme.colorScheme.primary
    val emptyBarColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val maxVal = (dailyCounts.values.maxOrNull() ?: 1).toFloat().coerceAtLeast(4f)

        val paddingBottom = 16.dp.toPx()
        val paddingTop = 6.dp.toPx()
        val spacing = 2.dp.toPx()

        val availableWidth = width - (maxDay - 1) * spacing
        val barWidth = availableWidth / maxDay

        for (day in 1..maxDay) {
            val count = dailyCounts[day] ?: 0
            val x = (day - 1) * (barWidth + spacing)

            // Draw empty guideline representation bar
            drawRoundRect(
                color = emptyBarColor,
                topLeft = Offset(x, paddingTop),
                size = Size(barWidth, height - paddingBottom - paddingTop),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            if (count > 0) {
                val barHeight = (height - paddingBottom - paddingTop) * (count.toFloat() / maxVal)
                val y = height - paddingBottom - barHeight

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
                )
            }
        }
    }

    // Days labels underneath
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("1日", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${maxDay / 2}日", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text("${maxDay}日", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun YearlyTrendCanvas(
    monthlyCounts: Map<Int, Int>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary
    val outlineBaselineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    val maxVal = (monthlyCounts.values.maxOrNull() ?: 1).toFloat().coerceAtLeast(5f)

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val paddingLeft = 10.dp.toPx()
        val paddingRight = 10.dp.toPx()
        val paddingTop = 10.dp.toPx()
        val paddingBottom = 16.dp.toPx()

        val totalBars = 12
        val availableWidth = width - paddingLeft - paddingRight
        val barGroupWidth = availableWidth / totalBars
        val barWidth = barGroupWidth * 0.65f
        val gap = barGroupWidth * 0.35f

        for (m in 1..12) {
            val count = monthlyCounts[m] ?: 0
            val x = paddingLeft + (m - 1) * barGroupWidth + gap / 2

            // Background baseline
            drawLine(
                color = outlineBaselineColor,
                start = Offset(x, height - paddingBottom),
                end = Offset(x + barWidth, height - paddingBottom),
                strokeWidth = 2.dp.toPx()
            )

            if (count > 0) {
                val barHeight = (height - paddingBottom - paddingTop) * (count.toFloat() / maxVal)
                val y = height - paddingBottom - barHeight

                // Draw gradient filled vertical bar
                drawRoundRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryColor, secondaryColor.copy(alpha = 0.7f))
                    ),
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }
    }

    // Months row bottom indicators
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 1..12) {
            Text(
                text = "${i}月",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.width(22.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun HistoricalLogCardItem(
    log: WorkoutLog,
    language: LanguageType,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val isCardio = log.category == "CARDIO"
    val isCustom = log.category == "CUSTOM" || log.isCustom
    val colorAccent = if (isCustom) {
        MaterialTheme.colorScheme.secondary
    } else if (isCardio) {
        MaterialTheme.colorScheme.primary
    } else {
        Color(0xFF8D5524)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEditClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val dateTextState = when(language) {
                        LanguageType.ENGLISH -> java.text.SimpleDateFormat("MMM dd", java.util.Locale.ENGLISH).format(java.util.Date(log.dateMillis))
                        else -> WorkoutViewModel.formatDateChinese(log.dateMillis)
                    }
                    Text(
                        text = dateTextState,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(
                        modifier = Modifier
                            .background(colorAccent.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        val catLabelText = if (isCustom) {
                            LanguageManager.getString("cat_custom", language)
                        } else if (isCardio) {
                            LanguageManager.getString("cat_cardio", language)
                        } else {
                            LanguageManager.getString("cat_strength", language)
                        }
                        Text(
                            text = catLabelText,
                            fontSize = 9.sp,
                            color = colorAccent,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LanguageManager.getExerciseName(log.exerciseName, language),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                val details = if (isCustom) {
                    "${log.customValue} ${log.customUnit}"
                } else if (isCardio) {
                    val unitKm = LanguageManager.getString("unit_distance", language)
                    val unitMins = if (log.customUnit.isNotEmpty() && log.customUnit != "分钟" && log.customUnit != "mins" && log.customUnit != "分") {
                        log.customUnit
                    } else {
                        LanguageManager.getString("unit_minutes", language)
                    }
                    val dist = if (log.distanceKm > 0.0) "${log.distanceKm} $unitKm • " else ""
                    "${dist}${log.durationMinutes} $unitMins"
                } else {
                    val unitSets = LanguageManager.getString("unit_sets", language)
                    val unitReps = LanguageManager.getString("unit_reps", language)
                    val unitWeight = if (log.customUnit.isNotEmpty() && log.customUnit != "分钟" && log.customUnit != "mins" && log.customUnit != "分") {
                        log.customUnit
                    } else {
                        LanguageManager.getString("unit_weight", language)
                    }
                    "${log.sets} $unitSets • ${log.reps} $unitReps • ${log.weightKg} $unitWeight"
                }
                Text(
                    text = details,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (log.caloriesBurned > 0) {
                    val labelKcal = LanguageManager.getString("unit_kcal", language)
                    val burnLabelText = when (language) {
                        LanguageType.ENGLISH -> "🔥 Burned: ${log.caloriesBurned} $labelKcal"
                        LanguageType.JAPANESE -> "🔥 消費: ${log.caloriesBurned} $labelKcal"
                        else -> "🔥 消耗: ${log.caloriesBurned} $labelKcal"
                    }
                    Text(
                        text = burnLabelText,
                        fontSize = 11.sp,
                        color = Color(0xFFFF5722),
                        fontWeight = FontWeight.Medium
                    )
                }

                if (log.notes.isNotEmpty()) {
                    Text(
                        text = "Notes: ${log.notes}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Row {
                IconButton(onClick = onEditClick) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = LanguageManager.getString("btn_update", language),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = LanguageManager.getString("btn_delete", language),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// Global Date Utilites
private fun getTodayStartMillis(): Long {
    val cal = Calendar.getInstance()
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    return cal.timeInMillis
}

private fun getLocalizedDayOfWeek(lang: LanguageType): String {
    val cal = Calendar.getInstance()
    val day = cal.get(Calendar.DAY_OF_WEEK)
    return when (lang) {
        LanguageType.CHINESE -> when (day) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            Calendar.SATURDAY -> "星期六"
            else -> ""
        }
        LanguageType.ENGLISH -> when (day) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
        LanguageType.JAPANESE -> when (day) {
            Calendar.SUNDAY -> "日曜日"
            Calendar.MONDAY -> "月曜日"
            Calendar.TUESDAY -> "火曜日"
            Calendar.WEDNESDAY -> "水曜日"
            Calendar.THURSDAY -> "木曜日"
            Calendar.FRIDAY -> "金曜日"
            Calendar.SATURDAY -> "土曜日"
            else -> ""
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        content()
    }
}

@Composable
fun ExerciseLibraryScreen(viewModel: WorkoutViewModel) {
    val currentLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val customExercises by viewModel.customExercisesList.collectAsStateWithLifecycle()
    
    var selectedCategoryTab by remember { mutableStateOf(0) } // 0: CARDIO, 1: STRENGTH
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .testTag("add_custom_exercise_fab")
                    .padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = LanguageManager.getString("library_add_btn", currentLanguage)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Header
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = LanguageManager.getString("library_title", currentLanguage),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = LanguageManager.getString("library_subtitle", currentLanguage),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Tab Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedCategoryTab = 0 }
                        .background(if (selectedCategoryTab == 0) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .padding(vertical = 10.dp)
                        .testTag("lib_tab_cardio"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = LanguageManager.getString("cat_cardio", currentLanguage),
                        color = if (selectedCategoryTab == 0) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedCategoryTab = 1 }
                        .background(if (selectedCategoryTab == 1) MaterialTheme.colorScheme.primary else Color.Transparent)
                        .padding(vertical = 10.dp)
                        .testTag("lib_tab_strength"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = LanguageManager.getString("cat_strength", currentLanguage),
                        color = if (selectedCategoryTab == 1) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Exercise List Content
            val currentCategory = if (selectedCategoryTab == 0) "CARDIO" else "STRENGTH"
            val presets = if (selectedCategoryTab == 0) viewModel.cardioExercises else viewModel.strengthExercises
            val filteredCustoms = customExercises.filter { it.category == currentCategory }

            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Preset items section header
                item {
                    Text(
                        text = LanguageManager.getString("preset_badge", currentLanguage),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(presets) { presetName ->
                    LibraryExerciseItemRow(
                        name = LanguageManager.getExerciseName(presetName, currentLanguage),
                        isCustom = false,
                        unit = null,
                        currentLanguage = currentLanguage,
                        onDeleteClick = {}
                    )
                }

                // Custom items section header if not empty
                if (filteredCustoms.isNotEmpty()) {
                    item {
                        Text(
                            text = LanguageManager.getString("custom_badge", currentLanguage),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 14.dp, bottom = 4.dp)
                        )
                    }

                    items(filteredCustoms) { customEx ->
                        LibraryExerciseItemRow(
                            name = customEx.name,
                            isCustom = true,
                            unit = customEx.unit,
                            currentLanguage = currentLanguage,
                            onDeleteClick = { viewModel.deleteCustomExercise(customEx) }
                        )
                    }
                }
                
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }

    if (showAddDialog) {
        AddCustomExerciseDialog(
            viewModel = viewModel,
            currentLanguage = currentLanguage,
            onDismiss = { showAddDialog = false }
        )
    }
}

@Composable
fun LibraryExerciseItemRow(
    name: String,
    isCustom: Boolean,
    unit: String?,
    currentLanguage: LanguageType,
    onDeleteClick: () -> Unit
) {
    val themeColor = if (isCustom) {
        MaterialTheme.colorScheme.secondary
    } else {
        MaterialTheme.colorScheme.primary
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(themeColor.copy(alpha = 0.1f), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isCustom) Icons.Default.Star else if (themeColor == MaterialTheme.colorScheme.primary) Icons.Default.PlayArrow else Icons.Default.Favorite,
                    contentDescription = null,
                    tint = themeColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(themeColor.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = LanguageManager.getString(if (isCustom) "custom_badge" else "preset_badge", currentLanguage),
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = themeColor
                        )
                    }
                }
                if (unit != null && unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${LanguageManager.getString("exercise_unit_label", currentLanguage)}: $unit",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (isCustom) {
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun AddCustomExerciseDialog(
    viewModel: WorkoutViewModel,
    currentLanguage: LanguageType,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("CARDIO") } // "CARDIO" or "STRENGTH"
    var unit by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = LanguageManager.getString("add_custom_exercise_title", currentLanguage),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Category Selection Chips
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = LanguageManager.getString("exercise_category_label", currentLanguage),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = "CARDIO" }
                                .background(if (category == "CARDIO") MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = LanguageManager.getString("cat_cardio", currentLanguage),
                                color = if (category == "CARDIO") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { category = "STRENGTH" }
                                .background(if (category == "STRENGTH") MaterialTheme.colorScheme.primary else Color.Transparent)
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = LanguageManager.getString("cat_strength", currentLanguage),
                                color = if (category == "STRENGTH") MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                // Name TextField
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(LanguageManager.getString("exercise_name_label", currentLanguage) + " *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Measurement Unit TextField
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedTextField(
                        value = unit,
                        onValueChange = { unit = it },
                        label = { Text(LanguageManager.getString("exercise_unit_label", currentLanguage)) },
                        placeholder = { Text(LanguageManager.getString("exercise_unit_placeholder", currentLanguage)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Quick select unit chips (次数、磅、分钟)
                    val quickUnits = listOf("次数", "磅", "分钟")
                    val quickUnitsDisplay = quickUnits.map { u ->
                        when (currentLanguage) {
                            LanguageType.ENGLISH -> when (u) {
                                "次数" -> "reps"
                                "磅" -> "lbs"
                                "分钟" -> "mins"
                                else -> u
                            }
                            LanguageType.JAPANESE -> when (u) {
                                "次数" -> "回"
                                "磅" -> "ポンド"
                                "分钟" -> "分"
                                else -> u
                            }
                            else -> u
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        quickUnitsDisplay.forEach { displayU ->
                            SuggestionChip(
                                onClick = { unit = displayU },
                                label = { Text(displayU) }
                            )
                        }
                    }
                }

                // Error Msg
                if (errorMsg != null) {
                    Text(
                        text = "⚠️ $errorMsg",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(LanguageManager.getString("btn_cancel", currentLanguage))
                    }
                    Button(
                        onClick = {
                            if (name.trim().isEmpty()) {
                                errorMsg = LanguageManager.getString("err_invalid_name", currentLanguage)
                            } else {
                                viewModel.insertCustomExercise(
                                    com.example.data.CustomExercise(
                                        name = name.trim(),
                                        category = category,
                                        unit = unit.trim().ifEmpty { null }
                                    )
                                )
                                onDismiss()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(LanguageManager.getString("dialog_title_add", currentLanguage))
                    }
                }
            }
        }
    }
}
