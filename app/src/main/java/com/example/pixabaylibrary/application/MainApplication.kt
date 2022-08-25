package com.example.pixabaylibrary.application

import android.app.Application
import com.example.pixabaylibrary.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application(){

    var TAG = "test"

    override fun onCreate() {
        initTimberLogger()
        super.onCreate()
    }


    private fun initTimberLogger() {
        if (BuildConfig.DEBUG) {
            // DebugTree has all usual logging functionality
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format("[L:%s] [%s:%s]",
                        element.lineNumber,
                        element.className,
                        element.methodName)
                }

                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
                    //change this tag for logging with filter in android studio...
                    super.log(priority, TAG,tag+ " >> "+ message, t)
//                    super.log(priority, TAG," >> "+ message,tag, t)
                }
            })
        }
    }


}