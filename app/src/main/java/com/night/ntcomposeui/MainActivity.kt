package com.night.ntcomposeui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mukesh.MarkDown
import com.night.ntcomposeui.component.*
import com.night.ntcomposeui.config.demoList
import com.night.ntcomposeui.ui.theme.NTComposeUITheme


class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            val isDarkMode = mainViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())

            NTComposeUITheme(darkTheme = isDarkMode.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    App()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
    }


}


@Composable
fun App() {
    val navHostController = rememberNavController()

    Scaffold(topBar = { AppTopBar() }) {
        NavHost(navController = navHostController, startDestination = "/main") {
            composable(route = "/main") { MainList(navHostController = navHostController) }

            demoList.forEachIndexed { index, demoItem ->
                composable(route = demoItem.routeName){ demoItem.ComposeView() }
            }
        }
    }
}

@Composable
fun AppTopBar(mainViewModel: MainViewModel = viewModel(modelClass = MainViewModel::class.java)) {

    val isDarkMode = mainViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
    var isMenuExpanded by remember { mutableStateOf(false)}
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(text = "Compose Demos") },
        actions = {
            IconButton(onClick = { isMenuExpanded = true }){
                Icon(Icons.Default.Menu,"")
            }
            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { isMenuExpanded = false }) {
                DropdownMenuItem(onClick = { }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Dark Mode")
                        Switch(checked = isDarkMode.value, onCheckedChange = {
                            mainViewModel.setIsDarkMode(it)
                        })
                    }
                }
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NTComposeUITheme {
        App()
    }
}

@Preview
@Composable
fun TestView() {

    val context = LocalContext.current
    NTComposeUITheme {
        MarkDown(text = context.assets.open("diceMd.md").readBytes().decodeToString())
    }
}