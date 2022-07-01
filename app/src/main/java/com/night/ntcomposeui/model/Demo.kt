package com.night.ntcomposeui.model

import androidx.compose.runtime.Composable
import com.night.ntcomposeui.component.DiceDemo
import com.night.ntcomposeui.component.MyWebView
import com.night.ntcomposeui.component.RealDiceRollerDemo
import com.night.ntcomposeui.demos.InfinityLoadingDemo
import com.night.ntcomposeui.demos.LoadImagesDemo
import com.night.ntcomposeui.demos.todo.TodosDemo


sealed class Demo(
    val title: String,
    val routeName: String,
    val ComposeView: @Composable () -> Unit,
    val mdFileName: String? = null,  //If have markdown file
//    val webDemoLink: String? = null
) {
    object Dice : Demo(
        title = "Dice",
        routeName = "/dice",
        ComposeView = { DiceDemo() },
        mdFileName = "diceMd.md"
    )

    object RealDice : Demo(
        "Real Dice",
        routeName = "/real_dice",
        ComposeView = { RealDiceRollerDemo() },
        mdFileName = "realDice.md"
    )

    object LoadImages : Demo(
        title = "Load Images",
        routeName = "/load_images",
        ComposeView = { LoadImagesDemo() },
        mdFileName = "loadImages.md"
    )

    object InfinityLoading : Demo(
        title = "Infinity Loading",
        routeName = "/infinity_loading",
        ComposeView = { InfinityLoadingDemo() },
        mdFileName = "infinityLoading.md"
    )

    object TodoMVVM : Demo(
        title = "Todo in MVVM",
        routeName = "/todo_mvvm",
        ComposeView = { TodosDemo() },
        mdFileName = "todoMVVM.md"
    )

    object DateWeb : Demo(
        title = "Date Web",
        routeName = "/date_web",
        ComposeView = { MyWebView(mUrl = "https://nightnzh.github.io/date-nzh/")  },
    )

    object ArielWebClone : Demo(
        title = "ArielWebClone",
        routeName = "/arie_web_clone",
        ComposeView = { MyWebView(mUrl = "https://62243aa50cf0490008b43641--friendly-kowalevski-f7f484.netlify.app/")  },
    )
}


