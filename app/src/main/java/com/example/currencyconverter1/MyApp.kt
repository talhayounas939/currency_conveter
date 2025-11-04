package com.example.currencyconverter1

import android.app.Application
import com.example.currencyconverter1.util.AppContextProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
     override fun onCreate() {
       super.onCreate()
         AppContextProvider.initialize(this)
         }
}