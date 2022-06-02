package com.night.ntcomposeui.modal

class Dice(val numSides: Int = 6) {
    fun roll() = (1..numSides).random()
}