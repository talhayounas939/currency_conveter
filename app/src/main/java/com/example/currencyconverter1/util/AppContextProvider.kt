package com.example.currencyconverter1.util

import android.content.Context

class AppContextProvider private constructor() {
  companion object {
        private var appContext: Context? = null

       fun initialize(context: Context) {
             this.appContext = context.applicationContext
            }

      val context: Context
        get() = checkNotNull(appContext) { "AppContextProvider not initialized!" }
        }
}