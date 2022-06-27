package com.night.ntcomposeui.config

import com.night.ntcomposeui.component.DiceDemo
import com.night.ntcomposeui.component.RealDiceRollerDemo
import com.night.ntcomposeui.demos.InfinityLoadingDemo
import com.night.ntcomposeui.demos.LoadImagesDemo
import com.night.ntcomposeui.demos.todo.TodosDemo
import com.night.ntcomposeui.model.DemoItem

const val IS_DARK_MODE = "is_dark_mode"

val demoList = listOf<DemoItem>(
    DemoItem("Dice", "/dice", { DiceDemo() } , "diceMd.md"),
    DemoItem("Real Dice", "/real_dice", { RealDiceRollerDemo() } , "diceMd.md"),
    DemoItem("Load Images", "/load_images", { LoadImagesDemo() } , "loadImages.md"),
    DemoItem("Infinity Loading", "/infinity_loading", { InfinityLoadingDemo() } ),
    DemoItem("Todo in MVVM", "/todo_mvvm", { TodosDemo() } ),
)