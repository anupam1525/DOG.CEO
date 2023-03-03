package com.ramsoft.poc.presentation

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.ramsoft.poc.R
import com.ramsoft.poc.data.RetrofitClient
import com.ramsoft.poc.databinding.ActivityMainBinding
import com.ramsoft.poc.repository.DogRepository
import com.ramsoft.poc.utils.GlideImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainActivityViewModel
    private var retrofitClient = RetrofitClient.getInstance()

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, MainActivityViewModelFactory(DogRepository(retrofitClient))
        )[MainActivityViewModel::class.java]

        setupView()
    }

    private fun setupView() {
        viewModel.dogImageResponse.observe(this) { response ->
            if (response.status == getString(R.string.success)) {
                loadImage(response.dogImage)
                imageUrl = response.dogImage
            } else {
                Toast.makeText(this, getString(R.string.api_error), Toast.LENGTH_SHORT).show()
            }
        }

        binding.downloadImage.setOnClickListener {
            saveImage()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this).load(imageUrl).listener(GlideImpl.OnCompleted {
                binding.progress.visibility = View.GONE
                binding.downloadImage.visibility = View.VISIBLE
            }).into(binding.greetImage)
    }

    private fun saveImage() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.saveImage(
                withContext(Dispatchers.IO) {
                    Glide.with(this@MainActivity).asBitmap().load(imageUrl).submit().get()
                }, applicationContext
            )
        }
    }
}