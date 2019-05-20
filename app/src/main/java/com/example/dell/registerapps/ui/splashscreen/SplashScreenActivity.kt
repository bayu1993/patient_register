package com.example.dell.registerapps.ui.splashscreen

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.example.dell.registerapps.R
import com.example.dell.registerapps.ui.main.MainActivity
import org.jetbrains.anko.startActivity
import kotlin.concurrent.thread

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }

        thread(start = true) {
            try {
                Thread.sleep(5000L)
            } catch (error: Throwable) {
                error.printStackTrace()
            } finally {
                startActivity<MainActivity>()
                this.finish()
            }
        }
    }
}
