package com.example.walmartwaletest

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.walmartwaletest.databinding.ActivityMainBinding
import com.example.walmartwaletest.viewModels.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels<MainActivityViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewModel = viewModel

        viewModel.getAPODResponse()
        observeResponse()

    }

    private fun observeResponse() {
        viewModel.response.observe(this) {
            binding.tvTitle.text = it.title
            Glide.with(this).load(it.url).into(binding.apodImageView)
            binding.description.text = it.explanation
        }
    }
}