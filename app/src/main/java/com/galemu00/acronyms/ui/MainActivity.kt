package com.galemu00.acronyms.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.galemu00.acronyms.R
import com.galemu00.acronyms.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}