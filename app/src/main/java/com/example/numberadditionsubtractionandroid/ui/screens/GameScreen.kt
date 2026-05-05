package com.example.numberadditionsubtractionandroid.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.AppStrings
import com.example.numberadditionsubtractionandroid.data.GameMode
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.game.GameViewModel
import com.example.numberadditionsubtractionandroid.ui.components.AdditionAnimalGrid
import com.example.numberadditionsubtractionandroid.ui.components.AnswerButtons
import com.example.numberadditionsubtractionandroid.ui.components.SubtractionAnimalGrid
import com.example.numberadditionsubtractionandroid.ui.theme.BackgroundColor
import com.example.numberadditionsubtractionandroid.ui.theme.CorrectGreen
import com.example.numberadditionsubtractionandroid.ui.theme.GrassGreen
import com.example.numberadditionsubtractionandroid.ui.theme.PrimaryColor
import com.example.numberadditionsubtractionandroid.ui.theme.WrongRed

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    settings: GameSettings,
    onGameOver: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = settings.language
    var showExitDialog by remember { mutableStateOf(false) }

    if (uiState.isGameOver) {
        onGameOver()
        return
    }

    val round = uiState.round ?: return

    val operatorSymbol = if (round.operator == GameMode.ADDITION) "+" else "\u2212"
    val operatorColor = if (round.operator == GameMode.ADDITION) GrassGreen else WrongRed

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // 進度列 + 退出按鈕
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = AppStrings.roundProgress(lang, uiState.currentRound, uiState.totalRounds),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryColor
                    )
                    Button(
                        onClick = { showExitDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        modifier = Modifier.height(36.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "退出測試"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了"
                            },
                            fontSize = 13.sp,
                            color = Color(0xFF555555)
                        )
                    }
                }
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { uiState.currentRound.toFloat() / uiState.totalRounds },
                    modifier = Modifier.fillMaxWidth(),
                    color = PrimaryColor
                )
                Spacer(Modifier.height(8.dp))

                // 算式列：與下方動物格子欄寬一致（weight1f + 48dp + weight1f）
                if (round.operator == GameMode.ADDITION) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // num1：對齊左側動物區域中心
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${round.num1}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                        }
                        // 運算符號：與下方加號圓圈同寬（48dp + 2×4dp padding）
                        Box(
                            modifier = Modifier.size(width = 56.dp, height = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = GrassGreen
                            )
                        }
                        // num2：對齊右側動物區域中心
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${round.num2}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                } else {
                    // 減法：比照加法的三欄位格式
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${round.num1}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1565C0)
                            )
                        }
                        Box(
                            modifier = Modifier.size(width = 56.dp, height = 36.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = operatorSymbol,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = operatorColor
                            )
                        }
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            Text(
                                text = "${round.num2}",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFE65100)
                            )
                        }
                    }
                }

                // 提示文字
                Text(
                    text = if (round.operator == GameMode.ADDITION) AppStrings.tapToAdd(lang)
                    else AppStrings.tapToCross(lang),
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                when (round.operator) {
                    GameMode.ADDITION -> AdditionAnimalGrid(
                        animals1 = round.animals1,
                        animals2 = round.animals2,
                        tappedIndices1 = round.tappedIndices1,
                        tappedIndices2 = round.tappedIndices2,
                        onTapGroup1 = { viewModel.tapAnimalGroup1(it) },
                        onTapGroup2 = { viewModel.tapAnimalGroup2(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    GameMode.SUBTRACTION -> SubtractionAnimalGrid(
                        animals = round.animals1,
                        crossedOutIndices = round.crossedOutIndices,
                        maxCrossOut = round.num2,
                        onCrossOut = { viewModel.crossOutAnimal(it) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    else -> {}
                }
            }

            Column {
                AnswerButtons(
                    answers = round.answers,
                    correctAnswer = round.correctAnswer,
                    selectedAnswer = round.selectedAnswer,
                    onSelect = { viewModel.selectAnswer(it) }
                )
            }
        }

        if (showExitDialog) {
            AlertDialog(
                onDismissRequest = { showExitDialog = false },
                title = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "退出測試"
                            AppLanguage.ENGLISH -> "Quit Game"
                            AppLanguage.JAPANESE -> "ゲームを終了"
                        },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = when (lang) {
                            AppLanguage.CHINESE -> "確定要退出測試嗎？\n目前的進度將不會被記錄。"
                            AppLanguage.ENGLISH -> "Are you sure you want to quit?\nYour current progress will not be saved."
                            AppLanguage.JAPANESE -> "本当にゲームを終了しますか？\n現在の進捗は記録されません。"
                        },
                        fontSize = 15.sp
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showExitDialog = false
                            onGameOver()
                        }
                    ) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "確定退出"
                                AppLanguage.ENGLISH -> "Quit"
                                AppLanguage.JAPANESE -> "終了する"
                            },
                            color = WrongRed,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showExitDialog = false }) {
                        Text(
                            text = when (lang) {
                                AppLanguage.CHINESE -> "繼續遊戲"
                                AppLanguage.ENGLISH -> "Keep Playing"
                                AppLanguage.JAPANESE -> "続ける"
                            },
                            color = PrimaryColor
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp)
            )
        }

        AnimatedVisibility(
            visible = uiState.showFeedback,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut(),
            modifier = Modifier.align(Alignment.Center)
        ) {
            val isCorrect = round.isCorrect == true
            Box(
                modifier = Modifier
                    .background(
                        color = if (isCorrect) CorrectGreen else WrongRed,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(horizontal = 48.dp, vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isCorrect) "\u2B50" else "\uD83D\uDE22",
                        fontSize = 48.sp
                    )
                    Text(
                        text = if (isCorrect) AppStrings.correct(lang) else AppStrings.wrong(lang),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    if (!isCorrect) {
                        Text(
                            text = "${round.correctAnswer}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

