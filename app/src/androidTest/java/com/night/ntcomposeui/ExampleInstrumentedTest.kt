package com.night.ntcomposeui

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.night.ntcomposeui.modal.Dice

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.night.ntcomposeui", appContext.packageName)
    }

    @Test
    fun DiceTest(){
        val dice = Dice(100)
        val rolledResult = dice.roll()
        assertTrue("The value of rollResult was not between 1 and ${dice.numSides}",rolledResult in 1..dice.numSides)
    }
}