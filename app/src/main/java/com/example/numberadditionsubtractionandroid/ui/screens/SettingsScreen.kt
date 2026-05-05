package com.example.numberadditionsubtractionandroid.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.numberadditionsubtractionandroid.data.AppLanguage
import com.example.numberadditionsubtractionandroid.data.AppStrings
import com.example.numberadditionsubtractionandroid.data.GameSettings
import com.example.numberadditionsubtractionandroid.data.LeaderboardRepository
import com.example.numberadditionsubtractionandroid.data.VoiceMode
import com.example.numberadditionsubtractionandroid.ui.theme.BackgroundColor
import com.example.numberadditionsubtractionandroid.ui.theme.OrangePeel
import com.example.numberadditionsubtractionandroid.ui.theme.PrimaryColor
import com.example.numberadditionsubtractionandroid.ui.theme.WrongRed
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settings: GameSettings,
    onSettingsChanged: (GameSettings) -> Unit,
    onBack: () -> Unit,
    leaderboardRepository: LeaderboardRepository
) {
    val lang = settings.language
    val scope = rememberCoroutineScope()
    var clearState by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        AppStrings.settings(lang),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = PrimaryColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Rounds
            SettingsCard {
                Text(
                    "${AppStrings.rounds(lang)}: ${settings.rounds}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = settings.rounds.toFloat(),
                    onValueChange = { onSettingsChanged(settings.copy(rounds = it.roundToInt())) },
                    valueRange = 3f..20f,
                    steps = 16
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("3", fontSize = 14.sp, color = Color.Gray)
                    Text("20", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Max Count
            SettingsCard {
                Text(
                    "${AppStrings.maxCount(lang)}: ${settings.maxCount}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Slider(
                    value = settings.maxCount.toFloat(),
                    onValueChange = { onSettingsChanged(settings.copy(maxCount = it.roundToInt())) },
                    valueRange = 5f..100f,
                    steps = 18
                )
                Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                    Text("5", fontSize = 14.sp, color = Color.Gray)
                    Text("100", fontSize = 14.sp, color = Color.Gray)
                }
            }

            // Voice mode
            SettingsCard {
                Text(AppStrings.voice(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        VoiceMode.NONE to AppStrings.voiceNone(lang),
                        VoiceMode.NUMBER to AppStrings.voiceNumber(lang),
                        VoiceMode.NUMBER_WITH_ANIMAL to AppStrings.voiceNumberWithAnimal(lang)
                    ).forEach { (mode, label) ->
                        val selected = settings.voiceMode == mode
                        Button(
                            onClick = { onSettingsChanged(settings.copy(voiceMode = mode)) },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) PrimaryColor else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = label, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }


            // Clear leaderboard
            SettingsCard {
                Text(
                    AppStrings.clearLeaderboard(lang),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = WrongRed
                )
                Spacer(Modifier.height(10.dp))

                when (clearState) {
                    0 -> Button(
                        onClick = { clearState = 1 },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = WrongRed)
                    ) {
                        Text("\uD83D\uDDD1\uFE0F  ${AppStrings.clearLeaderboard(lang)}", color = Color.White)
                    }

                    1 -> {
                        Text(AppStrings.clearConfirm1(lang), fontSize = 15.sp, color = Color(0xFF7B0000))
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { clearState = 0 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) { Text(AppStrings.cancel(lang), color = Color.White) }
                            Button(
                                onClick = { clearState = 2 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = WrongRed)
                            ) { Text(AppStrings.clearConfirmBtn(lang), color = Color.White) }
                        }
                    }

                    2 -> {
                        Text(
                            AppStrings.clearConfirm2(lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7B0000)
                        )
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = { clearState = 0 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                            ) { Text(AppStrings.cancel(lang), color = Color.White) }
                            Button(
                                onClick = {
                                    scope.launch {
                                        leaderboardRepository.clearAll()
                                        clearState = 3
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7B0000))
                            ) { Text(AppStrings.clearConfirmBtn(lang), color = Color.White) }
                        }
                    }

                    3 -> {
                        Text(
                            "\u2705 ${AppStrings.clearDone(lang)}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                        Spacer(Modifier.height(6.dp))
                        Button(
                            onClick = { clearState = 0 },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                        ) { Text(AppStrings.back(lang), color = Color.White) }
                    }
                }
            }

            // Language switcher
            SettingsCard {
                Text(AppStrings.language(lang), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        AppLanguage.CHINESE to "中文",
                        AppLanguage.ENGLISH to "English",
                        AppLanguage.JAPANESE to "日本語"
                    ).forEach { (l, label) ->
                        val selected = settings.language == l
                        Button(
                            onClick = { onSettingsChanged(settings.copy(language = l)) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selected) OrangePeel else Color.LightGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(label, color = Color.White, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

