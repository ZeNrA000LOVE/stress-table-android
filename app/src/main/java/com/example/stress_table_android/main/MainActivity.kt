package com.example.stress_table_android.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.core.navigaton.NavGraph
import com.example.stress_table_android.ui.theme.StresstableandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StresstableandroidTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Title") },
                            navigationIcon = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Menu, contentDescription = null)
                                }
                            },
                            actions = {
                                IconButton(onClick = { /* doSomething() */ }) {
                                    Icon(Icons.Filled.Favorite, contentDescription = null)
                                }
                            }
                        )
                    },
                    modifier = Modifier.fillMaxSize(),
                    content = { innerPadding ->
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize()
                        ) {
                            DisplayNav()
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DisplayNav() {
    // navControllerを作成
    val navController = rememberNavController()
    // NavHostを作成
    // startDestinationは最初に表示するページ
    // startDestinationは最初に表示するページ
    NavHost(
        navController = navController,
        startDestination = NavGraph.first
    ) {
        // 最初に表示するページ
        composable(route = NavGraph.first) {
            FirstScreen(navController = navController)
        }
        // 2番目に表示するページ
        composable(route = NavGraph.second) {
            SecondScreen(navController = navController)
        }
    }
}

@Composable
fun FirstScreen(navController: NavController) {
    val viewModel: MainViewModel = hiltViewModel()
    val uiState = viewModel.uiState

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        LazyColumn(
            Modifier.weight(1f)
        ) {
            items(uiState.stressTextList) {
                ListItemWithButton(
                    it,
                    uiState,
                    { viewModel.onTextSelected(it.text) },
                    viewModel::onTypeSelected
                )
            }
        }
        Button(onClick = { navController.navigate(NavGraph.second) }) {
            Text(text = "Go to 2nd Screen")
        }
        SendIconTextField(
            uiState,
            onValueChange = viewModel::onValueChange,
            onSendText = viewModel::addText
        )
    }
}

@Composable
fun SelectButtons(
    stress: Stress,
    uiState: MainUiState,
    onTypeSelected: (ButtonType) -> Unit
) {
    AnimatedVisibility(
        visible = stress.text == uiState.selectText,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column {
            Divider()
            Row {
                ButtonType.entries.forEach {
                    Button(
                        onClick = { onTypeSelected(it) },
                        modifier = Modifier
                            .padding(8.dp)
                            .weight(1f),
                        colors = if (stress.type == it) ButtonDefaults.buttonColors() else ButtonDefaults.elevatedButtonColors()
                    ) {
                        Text(text = it.text)
                    }
                }
            }
        }
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    Button(onClick = { navController.navigate(NavGraph.first) }) {
        Text(text = "Go to 1st Screen")
    }
}

@Composable
fun ListItemWithButton(
    stress: Stress,
    uiState: MainUiState,
    onTextSelected: () -> Unit,
    onTypeSelected: (ButtonType) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stress.text,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                modifier = Modifier.padding(start = 8.dp),
                onClick = onTextSelected
            ) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }
        }
        SelectButtons(stress, uiState, onTypeSelected)
    }
}

@Composable
fun SendIconTextField(
    uiState: MainUiState,
    onValueChange: (String) -> Unit,
    onSendText: () -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(bottom = 24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        TextField(
            value = uiState.text,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onSendText
        ) {
            Text("送信")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StresstableandroidTheme {
        DisplayNav()
    }
}
