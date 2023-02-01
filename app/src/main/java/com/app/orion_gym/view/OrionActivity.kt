package com.app.orion_gym.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.orion_gym.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orion)
    }
}