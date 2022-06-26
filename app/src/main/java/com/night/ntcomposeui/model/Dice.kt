package com.night.ntcomposeui.model

class Dice(val numSides: Int = 6) {
    fun roll() = (1..numSides).random()
}