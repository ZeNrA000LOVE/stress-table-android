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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.core.navigaton.NavGraph
import com.example.stress_table_android.R
import com.example.stress_table_android.ui.theme.StresstableandroidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val currentBackStack by navController.currentBackStackEntryAsState()
            val current = currentBackStack?.destination?.route

            StresstableandroidTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "StressTable") },
                            navigationIcon = {
                                when (current) {
                                    NavGraph.description -> {
                                        IconButton(onClick = { navController.popBackStack() }) {
                                            Icon(Icons.Filled.ArrowBack, contentDescription = null)
                                        }
                                    }
                                }
                            },
                            actions = {
                                when (current) {
                                    NavGraph.main -> {
                                        IconButton(onClick = { navController.navigate(NavGraph.description) }) {
                                            Icon(Icons.Filled.Add, contentDescription = null)
                                        }
                                    }
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
                            DisplayNav(navController)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun DisplayNav(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavGraph.main
    ) {
        // 最初に表示するページ
        composable(route = NavGraph.main) {
            MainScreen()
        }
        // 2番目に表示するページ
        composable(route = NavGraph.description) {
            DescriptionScreen()
        }
    }
}

@Composable
fun MainScreen() {
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
fun DescriptionScreen() {
    Text(text = stringResource(R.string.description))
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
