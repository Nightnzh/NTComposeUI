package com.night.ntcomposeui.config

import com.night.ntcomposeui.component.DiceDemo
import com.night.ntcomposeui.component.RealDiceRollerDemo
import com.night.ntcomposeui.modal.DemoItem


val demoList = listOf<DemoItem>(
    DemoItem("Dice", "/dice", { DiceDemo() } , "diceMd.md"),
    DemoItem("Real Dice", "/real_dice", { RealDiceRollerDemo() } , "diceMd.md"),
)