package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.WorkoutDatabase
import com.example.data.WorkoutRepository
import com.example.ui.screens.AppNavigationWrapper
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.WorkoutViewModel
import com.example.ui.viewmodel.WorkoutViewModelFactory

class MainActivity : ComponentActivity() {

    // Instantiate local database and repository singletons manually
    private val database by lazy { WorkoutDatabase.getDatabase(this) }
    private val repository by lazy { WorkoutRepository(database.workoutDao()) }

    // Lazy instantiate ViewModel using custom Factory
    private val viewModel: WorkoutViewModel by viewModels {
        WorkoutViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup border edge padding transparency
        enableEdgeToEdge()

        viewModel.loadSettings(this)

        setContent {
            val themeType by viewModel.selectedTheme.collectAsState()
            MyApplicationTheme(themeType = themeType) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigationWrapper(viewModel = viewModel)
                }
            }
        }
    }
}
