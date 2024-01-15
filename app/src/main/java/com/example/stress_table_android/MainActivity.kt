package com.example.stress_table_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.stress_table_android.ui.theme.StresstableandroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StresstableandroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
    DisplayNav()
}

@Composable
fun DisplayNav() {
    // navControllerを作成
    val navController = rememberNavController()
    // NavHostを作成
    // startDestinationは最初に表示するページ
    // startDestinationは最初に表示するページ
    NavHost(navController = navController,
        startDestination = "First Screen") {
        // 最初に表示するページ
        composable(route = "First Screen") {
            FirstScreen(navController = navController)
        }
        // 2番目に表示するページ
        composable(route = "Second Screen") {
            SecondScreen(navController = navController)
        }
    }
}

@Composable
fun FirstScreen(navController: NavController) {
    Button(onClick = { navController.navigate("Second Screen") }) {
        Text(text = "Go to 2nd Screen")
    }
}

@Composable
fun SecondScreen(navController: NavController) {
    Button(onClick = { navController.navigate("First Screen")}) {
        Text(text = "Go to 1st Screen")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    StresstableandroidTheme {
        Greeting("Android")
    }
}