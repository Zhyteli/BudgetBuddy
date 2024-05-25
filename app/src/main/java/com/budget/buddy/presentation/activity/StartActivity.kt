package com.budget.buddy.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.input.key.Key.Companion.U
import androidx.lifecycle.lifecycleScope
import com.budget.buddy.domain.cash.usecase.maindatauser.LoadDataMainUserDataMouthUseCase
import com.budget.buddy.domain.cash.usecase.maindatauser.SaveDataMainUserDataMouthUseCase
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.presentation.ui.start.StartScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : ComponentActivity() {

    @Inject
    lateinit var saveDataMainUserDataMouthUseCase: SaveDataMainUserDataMouthUseCase

    @Inject
    lateinit var loadDataMainUserDataMouthUseCase: LoadDataMainUserDataMouthUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        // Enable sticky immersive mode
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        setContent {
            StartScreen {
                lifecycleScope.launch {
                    saveDataMainUserDataMouthUseCase(it)
                }
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
