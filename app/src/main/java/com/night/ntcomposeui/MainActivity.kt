package com.night.ntcomposeui

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mukesh.MarkDown
import com.night.ntcomposeui.component.*
import com.night.ntcomposeui.ui.theme.NTComposeUITheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NTComposeUITheme() {
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
}


@Composable
fun App() {
    val navHostController = rememberNavController()
    val ctx = LocalContext.current
    Scaffold(topBar = { AppTopBar() }) {
        NavHost(navController = navHostController, startDestination = "/main") {
            composable(route = "/main") { MainList(navHostController = navHostController) }
            composable(route = "/dice") { DiceDemo() }
            composable(route = "/real_dice_roller") { RealDiceRollerDemo() }
        }
    }
}

@Composable
fun AppTopBar(scaffoldState: ScaffoldState? = null) {

    TopAppBar(
        title = { Text(text = "NTCompose Demo") },
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