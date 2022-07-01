package com.night.ntcomposeui

import android.app.Application
import android.content.res.Configuration
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.night.ntcomposeui.config.IS_DARK_MODE
import com.night.ntcomposeui.model.Demo
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {


    private val app = (application as NTCApplication)
    private val dataStore = app.dataStore

    //isDartMode flow from dataStore
    val isDarkMode = app.dataStore.data.map{
        it[booleanPreferencesKey(IS_DARK_MODE)] ?: (application.resources.configuration.uiMode == Configuration.UI_MODE_NIGHT_MASK)
    }

    //setDarkMode
    fun setIsDarkMode(value : Boolean) = viewModelScope.launch {
        dataStore.edit {
            it[booleanPreferencesKey(IS_DARK_MODE)] = value
        }
    }

    //demo list
    val demoList = listOf<Demo>(
        Demo.Dice,
        Demo.RealDice,
        Demo.LoadImages,
        Demo.InfinityLoading,
        Demo.TodoMVVM
    )


    //Web demo list
    val webDemoList = listOf<Demo>(
        Demo.DateWeb,
        Demo.ArielWebClone
    )


}