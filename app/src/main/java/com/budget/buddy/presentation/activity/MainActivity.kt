package com.budget.buddy.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen
import androidx.lifecycle.lifecycleScope
import com.budget.buddy.R
import com.budget.buddy.data.database.getdata.UserDataState
import com.budget.buddy.domain.items.SpendingItem
import com.budget.buddy.domain.mapper.convert.ConvertTime
import com.budget.buddy.domain.user.MainUserDataMouth
import com.budget.buddy.presentation.ui.additem.BottomSheetComponent
import com.budget.buddy.presentation.ui.additem.EditItem
import com.budget.buddy.presentation.ui.anim.AnimatedPreloader
import com.budget.buddy.presentation.ui.card.CardCashTransaction
import com.budget.buddy.presentation.ui.list.ItemsList
import com.budget.buddy.presentation.ui.menu.analysis.AnalysisCard
import com.budget.buddy.presentation.ui.menu.history.HistoryCard
import com.budget.buddy.presentation.ui.them.Colors
import com.budget.buddy.presentation.view.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

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
            SplashScreen {
                MonthlyCheck()
            }
        }
    }

    @Composable
    fun SplashScreen(main: @Composable () -> Unit) {
        val show = remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(1000)
            show.value = true
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Colors.Background)
        ) {
            if (!show.value) {
                AnimatedPreloader(
                    raw = R.raw.anm,
                    modifier = Modifier
                        .size(200.dp)
                        .align(Alignment.Center)
                )
            } else {
                Box(modifier = Modifier.align(Alignment.Center)) {
                    main()
                }
            }
        }
    }

    @Composable
    private fun MainActivity.MonthlyCheck() {
        val resultInitialUserData by viewModel.userData.collectAsState()
        var dataUpdate by remember { mutableStateOf(true) }

        when (resultInitialUserData) {
            is UserDataState.Loading -> {}
            is UserDataState.Success -> {
                val mainUserDataMouth = (resultInitialUserData as UserDataState.Success).data

                if (mainUserDataMouth != null) {
                    if (dataUpdate) {
                        viewModel.checkingAvailabilityDataFromBank()
                        dataUpdate = false
                    }
                    val resultMono = viewModel.resultUserDataApiLive.observeAsState()

                    // Log the resultMono value directly
                    Log.d("TEST_Check", "Result Mono: ${resultMono.value}")

                    resultMono.value?.let {
                        if (it.isEmpty()) {
                            Log.d("TEST_", "Cash: ${it}")
                            getBankData()
                        }
                        MainScreen(it, mainUserDataMouth, resultMono = resultMono)
                    }
                    viewModel.spendingCounter(mainUserDataMouth)
                } else {
                    startActivity(Intent(this, StartActivity::class.java))
                    finish()
                }

            }

            is UserDataState.Error -> {
                val errorMessage = (resultInitialUserData as UserDataState.Error).message
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    @Composable
    private fun getBankData() {
        viewModel.getMonoData(application = application, callbackError = {
            lifecycleScope.launch(Dispatchers.Main) {
                Log.d("TEST_", "onCreate: ${it}")
                Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
            }
        })
    }

    @Composable
    private fun MainScreen(
        value: MutableList<SpendingItem>? = null,
        monthlyBudget: MainUserDataMouth = MainUserDataMouth(),
        resultMono: State<MutableList<SpendingItem>?>,
        spent: Double = 0.0,
        balance: Double = 0.0,
    ) {
        var showBottomSheet = remember { mutableStateOf(false) }
        val spendingItems by viewModel.spendingItems.collectAsState()
        val spentAll by viewModel.spentAll.observeAsState()
        val balanceAll by viewModel.liveBalance.observeAsState()
        var editShow = remember { mutableStateOf(false) }
        var historyShow = remember { mutableStateOf(false) }
        var analysisShow = remember { mutableStateOf(false) }
        val newSpendingItem = remember { mutableStateOf(SpendingItem()) }
        var showMenu = remember { mutableStateOf(false) } // State to control menu visibility
        val analysisAi by viewModel.livePromt.observeAsState()
        var currentMonth by remember { mutableStateOf(LocalDate.now().withDayOfMonth(1)) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Colors.Background)
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = monthlyBudget.name,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(10.dp),
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.open))
                    )
                    IconButton(
                        onClick = { showMenu.value = !showMenu.value }
                    ) {
                        Icon(
                            tint = Color.White,
                            imageVector = Icons.Filled.Menu,
                            contentDescription = "Menu",
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
                AnimatedVisibility(
                    visible = showMenu.value,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Button(
                            onClick = {
                                showMenu.value = false
                                historyShow.value = !historyShow.value
                            },
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = stringResource(R.string.history),
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.open))
                            )
                        }
                        Button(
                            onClick = {
                                showMenu.value = false
                                analysisShow.value = !analysisShow.value
                                viewModel.ai(application = application)
                            },
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = stringResource(R.string.recommendations),
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.open))
                            )
                        }
                        Button(
                            onClick = {
                                showMenu.value = false
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        WebActivity::class.java
                                    )
                                )
                            },
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = stringResource(R.string.bank),
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.open))
                            )
                        }
                        Button(
                            onClick = {
                                showMenu.value = false
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        StartActivity::class.java
                                    )
                                )
                            },
                            modifier = Modifier.wrapContentSize(Alignment.Center),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                        ) {
                            Text(
                                text = stringResource(R.string.new_),
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.open))
                            )
                        }
                    }
                }
                CardCashTransaction(
                    monthlyBudget = monthlyBudget.balance,
                    spent = spentAll ?: spent,
                    balance = balanceAll ?: balance
                )
                value?.let { viewModel.updateSpendingItems(it) }
                if (spendingItems.isNotEmpty()) {
                    Log.d("TEST_ItemsList", spendingItems.toString())
                    ItemsList(
                        items = spendingItems.filter {
                            ConvertTime.convertTimestampToDate(it.time) == currentMonth.monthValue.toString()
                        },
                        onDelete = { viewModel.deleteSpendingItem(it) },
                        onEdit = {
                            newSpendingItem.value = it
                            editShow.value = !editShow.value
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .wrapContentSize(),
                shape = RoundedCornerShape(7.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 10.dp
                )
            ) {
                Box(Modifier.background(Color.DarkGray)) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = null,
                        modifier = Modifier
                            .wrapContentWidth()
                            .padding(10.dp)
                            .clickable {
                                showBottomSheet.value = !showBottomSheet.value
                            },
                        tint = Color.White
                    )

                }
            }
        }

        BottomSheetComponent(
            isVisible = showBottomSheet.value,
            onDismiss = { showBottomSheet.value = false },
            addNewItem = {
                // Use the ViewModel function only to add the new item
                viewModel.addSpendingItems(it)
                showBottomSheet.value = false
            }
        )
        EditItem(editShow.value, item = newSpendingItem, onDismiss = { editShow.value = false },
            addNewItem = {
                viewModel.editSpendingItem(it)
                editShow.value = false
            })

        resultMono.value?.let {
            HistoryCard(
                historyShow.value, onDismiss = { historyShow.value = false },
                it
            )
        }
        AnalysisCard(analysisShow.value, onDismiss = { analysisShow.value = false }, analysisAi)
    }

}
