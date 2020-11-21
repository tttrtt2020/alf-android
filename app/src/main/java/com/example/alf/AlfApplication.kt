package com.example.alf

import android.app.Application
import java.util.*

class AlfApplication: Application() {

    companion object {
        private var appProperties: Properties = Properties()

        fun getProperty(propertyName: String): String {
            return appProperties.getProperty(propertyName)
        }
    }

    override fun onCreate() {
        super.onCreate()

        appProperties.load(assets.open("alf.properties"))
    }
}