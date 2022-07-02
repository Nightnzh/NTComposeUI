package com.night.ntcomposeui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mukesh.MarkDown
import com.night.ntcomposeui.component.*
import com.night.ntcomposeui.ui.theme.NTComposeUITheme


class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {

            val isDarkMode =
                mainViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())

            NTComposeUITheme(darkTheme = isDarkMode.value) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background,
                ) {
                    App(mainViewModel)
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


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun App(mainViewModel: MainViewModel) {

    val navHostController = rememberNavController()

    Scaffold(
        topBar = { AppTopBar(navHostController = navHostController) },
    ) {
        NavHost(navController = navHostController, startDestination = "/main") {
            composable(route = "/main") { MainList(navHostController = navHostController,mainViewModel) }
            composable(route = "/learning") { LeaningNotionList() }

            mainViewModel.demoList.forEachIndexed { index, demoItem ->
                composable(route = demoItem.routeName) { demoItem.ComposeView() }
            }

            mainViewModel.webDemoList.forEachIndexed { index, demoItem ->
                composable(route = demoItem.routeName) { demoItem.ComposeView() }
            }

        }
    }
}

@Composable
fun AppTopBar(
    mainViewModel: MainViewModel = viewModel(modelClass = MainViewModel::class.java),
    navHostController: NavHostController
) {

    val ctx = LocalContext.current
    val isDarkMode = mainViewModel.isDarkMode.collectAsState(initial = isSystemInDarkTheme())
    var isMenuExpanded by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = { Text(text = "Compose Demos") },
        actions = {
            IconButton(onClick = { isMenuExpanded = true }) {
                Icon(Icons.Default.MoreVert, "")
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
                DropdownMenuItem(onClick = {
                    navHostController.navigate("/learning")
                    isMenuExpanded = false
                }) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("Learning List")
                    }
                }
            }
        }
    )
}


@Composable
fun LeaningNotionList() {
    val mUrl = "https://nightxu.notion.site/48e63ed7e554490cb0e4dc48bea2b2ad?v=a961b5454a8f4c54a8624fb47f91a1b7"
    MyWebView(mUrl)
}




@Preview
@Composable
fun TestView() {
    val context = LocalContext.current
    NTComposeUITheme {
        MarkDown(text = context.assets.open("diceMd.md").readBytes().decodeToString())
    }
}