package com.example.cleanarchitectureexample.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.cleanarchitectureexample.presentation.composables.MainUIContentComposable
import com.example.cleanarchitectureexample.ui.theme.CleanArchitectureExampleTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val  viewModel by viewModels<UserViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUsers()

        //viewModel.getUserById(id = 5)

        setContent {
            val state  by  viewModel.userDataState.collectAsStateWithLifecycle()
            val context = LocalContext.current
            //val error = rememberFlowWithLifecycle(flow = viewModel.errorEventsSharedFlow)

            LaunchedEffect(Unit) {
                viewModel.errors.collectLatest {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }

            CleanArchitectureExampleTheme {
                MainUIContentComposable(
                    state = state,
                    onBackPress = {
                        onBackPressedDispatcher.onBackPressed()
                    }
                )
            }
        }
    }
}
