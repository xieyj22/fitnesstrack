package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.WorkoutLog
import com.example.data.WorkoutRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WorkoutViewModel(private val repository: WorkoutRepository) : ViewModel() {

    // Predefined lists of popular fitness activities
    val cardioExercises = listOf("跑步", "步行", "骑行", "游泳", "跳绳", "椭圆机", "划船机", "爬楼梯")
    val strengthExercises = listOf("杠铃卧推", "哑铃飞鸟", "深蹲", "硬拉", "引体向上", "俯卧撑", "哑铃弯举", "卷腹", "仰卧起坐", "肩部推举")

    val allWorkouts: StateFlow<List<WorkoutLog>> = repository.allWorkouts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val customExercisesList: StateFlow<List<com.example.data.CustomExercise>> = repository.allCustomExercises
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val allCardioExercises: StateFlow<List<String>> = combine(allWorkouts, customExercisesList) { _, customList ->
        cardioExercises + customList.filter { it.category == "CARDIO" }.map { it.name }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = cardioExercises)

    val allStrengthExercises: StateFlow<List<String>> = combine(allWorkouts, customExercisesList) { _, customList ->
        strengthExercises + customList.filter { it.category == "STRENGTH" }.map { it.name }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = strengthExercises)

    // Selection States for Analytics
    val currentCalendar: Calendar = Calendar.getInstance()
    val selectedYear = MutableStateFlow(currentCalendar.get(Calendar.YEAR))
    val selectedMonth = MutableStateFlow(currentCalendar.get(Calendar.MONTH) + 1) // 1 to 12

    // Filtered lists based on month/year
    val monthlyWorkouts: StateFlow<List<WorkoutLog>> = combine(allWorkouts, selectedYear, selectedMonth) { logs, year, month ->
        logs.filter { log ->
            getYearFromMillis(log.dateMillis) == year && getMonthFromMillis(log.dateMillis) == month
        }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    val yearlyWorkouts: StateFlow<List<WorkoutLog>> = combine(allWorkouts, selectedYear) { logs, year ->
        logs.filter { log ->
            getYearFromMillis(log.dateMillis) == year
        }
    }.stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    // Form inputs state
    val formIsEditing = MutableStateFlow(false)
    val formEditingId = MutableStateFlow<Int?>(null)
    val formCategory = MutableStateFlow("CARDIO") // "CARDIO" or "STRENGTH"
    val formExerciseName = MutableStateFlow("跑步")
    val formDateMillis = MutableStateFlow(System.currentTimeMillis())
    val formDurationMinutes = MutableStateFlow("")
    val formDistanceKm = MutableStateFlow("")
    val formWeightKg = MutableStateFlow("")
    val formReps = MutableStateFlow("")
    val formSets = MutableStateFlow("")
    val formCaloriesBurned = MutableStateFlow("")
    val formNotes = MutableStateFlow("")
    val formCustomUnit = MutableStateFlow("分钟")
    val formCustomValue = MutableStateFlow("")

    val showFormDialog = MutableStateFlow(false)

    // User preference states: Theme and Language
    val selectedTheme = MutableStateFlow(com.example.ui.theme.ThemeType.SAGE_ORGANIC)
    val selectedLanguage = MutableStateFlow(com.example.ui.translation.LanguageType.CHINESE)

    fun selectTheme(theme: com.example.ui.theme.ThemeType, context: android.content.Context? = null) {
        selectedTheme.value = theme
        context?.let {
            val prefs = it.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("selected_theme", theme.name).apply()
        }
    }

    fun selectLanguage(lang: com.example.ui.translation.LanguageType, context: android.content.Context? = null) {
        selectedLanguage.value = lang
        context?.let {
            val prefs = it.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
            prefs.edit().putString("selected_language", lang.name).apply()
        }
    }

    fun loadSettings(context: android.content.Context) {
        val prefs = context.getSharedPreferences("app_settings", android.content.Context.MODE_PRIVATE)
        val themeStr = prefs.getString("selected_theme", null)
        val langStr = prefs.getString("selected_language", null)
        
        themeStr?.let {
            try {
                selectedTheme.value = com.example.ui.theme.ThemeType.valueOf(it)
            } catch (e: Exception) {}
        }
        langStr?.let {
            try {
                selectedLanguage.value = com.example.ui.translation.LanguageType.valueOf(it)
            } catch (e: Exception) {}
        }
    }

    // Form validation
    val formError = MutableStateFlow<String?>(null)

    fun openAddDialog() {
        formIsEditing.value = false
        formEditingId.value = null
        formCategory.value = "CARDIO"
        formExerciseName.value = cardioExercises.first()
        formDateMillis.value = System.currentTimeMillis()
        formDurationMinutes.value = ""
        formDistanceKm.value = ""
        formWeightKg.value = ""
        formReps.value = ""
        formSets.value = ""
        formCaloriesBurned.value = ""
        formNotes.value = ""
        formCustomUnit.value = "分钟"
        formCustomValue.value = ""
        formError.value = null
        showFormDialog.value = true
    }

    fun openEditDialog(log: WorkoutLog) {
        formIsEditing.value = true
        formEditingId.value = log.id
        formCategory.value = log.category
        formExerciseName.value = log.exerciseName
        formDateMillis.value = log.dateMillis
        formDurationMinutes.value = if (log.durationMinutes > 0) log.durationMinutes.toString() else ""
        formDistanceKm.value = if (log.distanceKm > 0.0) log.distanceKm.toString() else ""
        formWeightKg.value = if (log.weightKg > 0.0) log.weightKg.toString() else ""
        formReps.value = if (log.reps > 0) log.reps.toString() else ""
        formSets.value = if (log.sets > 0) log.sets.toString() else ""
        formCaloriesBurned.value = if (log.caloriesBurned > 0) log.caloriesBurned.toString() else ""
        formNotes.value = log.notes
        formCustomUnit.value = log.customUnit
        formCustomValue.value = if (log.isCustom || log.customValue > 0.0) log.customValue.toString() else ""
        formError.value = null
        showFormDialog.value = true
    }

    fun onCategoryChange(category: String) {
        formCategory.value = category
        if (category == "CARDIO") {
            formExerciseName.value = cardioExercises.first()
        } else if (category == "STRENGTH") {
            formExerciseName.value = strengthExercises.first()
        } else {
            formExerciseName.value = "" // Custom Project Name starts empty
            formCustomUnit.value = "分钟"
            formCustomValue.value = ""
        }
    }

    fun saveWorkout() {
        if (formExerciseName.value.trim().isEmpty()) {
            formError.value = "请输入或选择运动名称"
            return
        }

        val parsedDuration = formDurationMinutes.value.toIntOrNull() ?: 0
        val parsedDistance = formDistanceKm.value.toDoubleOrNull() ?: 0.0
        val parsedWeight = formWeightKg.value.toDoubleOrNull() ?: 0.0
        val parsedReps = formReps.value.toIntOrNull() ?: 0
        val parsedSets = formSets.value.toIntOrNull() ?: 0
        val parsedCalories = formCaloriesBurned.value.toIntOrNull() ?: 0
        val parsedCustomValue = formCustomValue.value.toDoubleOrNull() ?: 0.0

        // Validation based on category
        if (formCategory.value == "CARDIO" && parsedDuration <= 0) {
            formError.value = "有氧运动请填写有效的时长（分钟）"
            return
        }
        if (formCategory.value == "STRENGTH" && parsedSets <= 0) {
            formError.value = "力量训练请填写有效的组数"
            return
        }
        if (formCategory.value == "CUSTOM" && parsedCustomValue <= 0.0) {
            formError.value = "自定义运动请填写有效的数值"
            return
        }

        val log = WorkoutLog(
            id = formEditingId.value ?: 0,
            dateMillis = formDateMillis.value,
            category = formCategory.value,
            exerciseName = formExerciseName.value.trim(),
            durationMinutes = if (formCategory.value == "CUSTOM" && formCustomUnit.value == "分钟") parsedCustomValue.toInt() else if (formCategory.value == "CUSTOM" && formCustomUnit.value == "小时") (parsedCustomValue * 60).toInt() else parsedDuration,
            distanceKm = parsedDistance,
            weightKg = parsedWeight,
            reps = parsedReps,
            sets = parsedSets,
            caloriesBurned = parsedCalories,
            notes = formNotes.value.trim(),
            isCustom = formCategory.value == "CUSTOM",
            customUnit = formCustomUnit.value,
            customValue = parsedCustomValue
        )

        viewModelScope.launch {
            if (formIsEditing.value) {
                repository.update(log)
            } else {
                repository.insert(log)
            }
            showFormDialog.value = false
        }
    }

    fun deleteWorkout(log: WorkoutLog) {
        viewModelScope.launch {
            repository.delete(log)
        }
    }

    fun deleteWorkoutById(id: Int) {
        viewModelScope.launch {
            repository.deleteById(id)
        }
    }

    fun insertCustomExercise(exercise: com.example.data.CustomExercise) {
        viewModelScope.launch {
            repository.insertCustomExercise(exercise)
        }
    }

    fun deleteCustomExercise(exercise: com.example.data.CustomExercise) {
        viewModelScope.launch {
            repository.deleteCustomExercise(exercise)
        }
    }

    fun setYear(year: Int) {
        selectedYear.value = year
    }

    fun setMonth(month: Int) {
        selectedMonth.value = month
    }

    fun incrementMonth() {
        if (selectedMonth.value == 12) {
            selectedMonth.value = 1
            selectedYear.value += 1
        } else {
            selectedMonth.value += 1
        }
    }

    fun decrementMonth() {
        if (selectedMonth.value == 1) {
            selectedMonth.value = 12
            selectedYear.value -= 1
        } else {
            selectedMonth.value -= 1
        }
    }

    fun incrementYear() {
        selectedYear.value += 1
    }

    fun decrementYear() {
        selectedYear.value -= 1
    }

    // Helper Date extraction functions using standard java.util.Calendar
    companion object {
        fun getYearFromMillis(millis: Long): Int {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return cal.get(Calendar.YEAR)
        }

        fun getMonthFromMillis(millis: Long): Int {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return cal.get(Calendar.MONTH) + 1 // 1-indexed
        }

        fun getDayFromMillis(millis: Long): Int {
            val cal = Calendar.getInstance()
            cal.timeInMillis = millis
            return cal.get(Calendar.DAY_OF_MONTH)
        }

        fun formatDate(millis: Long): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date(millis))
        }

        fun formatDateChinese(millis: Long): String {
            val sdf = SimpleDateFormat("MM月dd日", Locale.getDefault())
            return sdf.format(Date(millis))
        }
    }
}

class WorkoutViewModelFactory(private val repository: WorkoutRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkoutViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkoutViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
