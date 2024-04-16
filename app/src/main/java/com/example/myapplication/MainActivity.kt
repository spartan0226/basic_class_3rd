package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.databinding.ActivityMainBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var job: Job? = null
    private val TAG = "MainActivity"
    private var counter = 1
    private var randomValue = (1..100).random()
    private var isClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG, "onCreate")
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (savedInstanceState != null) {
            randomValue = savedInstanceState.getInt("randomValue")
            counter = savedInstanceState.getInt("counter")
            if (counter > 100) {
                counter = 100
            }
            binding.spartaTextView.text = counter.toString()

            isClicked = savedInstanceState.getBoolean("isClicked")
            if (isClicked) {
                binding.spartaTextView.text = counter.toString()
            }
        }

        setupButton()
        setRandomValueBetweenOneToHundred()
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        job?.cancel()
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart")
        setJobAndLaunch()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.i(TAG, "onRestoreInstanceState")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i(TAG, "onSaveInstanceState")
        outState.putInt("counter", counter)
        outState.putInt("randomValue", randomValue)
        outState.putBoolean("isClicked", isClicked)
    }

    private fun setupButton() {
        binding.clickButton.setOnClickListener {
            checkAnswerAndShowToast()
            job?.cancel()
            isClicked = true
        }
    }

    private fun setRandomValueBetweenOneToHundred() {
        binding.textViewRandom.text = randomValue.toString()
    }

    private fun setJobAndLaunch() {
        job?.cancel()
        job = lifecycleScope.launch {
            if (isActive) {
                while (counter <= 100) {
                    binding.spartaTextView.text = counter.toString()
                    if (isClicked) {
                        break
                    }
                    delay(500)
                    counter++
                }
            }
        }
    }

    private fun checkAnswerAndShowToast() {
        if (binding.spartaTextView.text.toString() == binding.textViewRandom.text.toString()) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this@MainActivity, SecondActivity::class.java))
        } else {
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show()
        }
    }
}