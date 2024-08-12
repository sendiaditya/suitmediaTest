package com.magang.suitmediatest

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val palindromeEditText = findViewById<EditText>(R.id.palindrome)
        val checkButton = findViewById<Button>(R.id.checkButton)
        val nextButton = findViewById<Button>(R.id.nextButton)


        checkButton.setOnClickListener {
            val inputText = palindromeEditText.text.toString()
            val sanitizedInput = inputText.filter { it.isLetterOrDigit() }.lowercase(Locale.getDefault())

            if (sanitizedInput.isNotEmpty() && sanitizedInput == sanitizedInput.reversed()) {
                showDialog("isPalindrome")
            } else {
                showDialog("not palindrome")
            }
        }

        nextButton.setOnClickListener {
            val name = findViewById<EditText>(R.id.name).text.toString()
            val intent = Intent(this, SecondActivity::class.java)

            if (name.isNotEmpty()) {
                intent.putExtra("name", name)
                val sharedPreferences = getSharedPreferences("userPref", MODE_PRIVATE)
                sharedPreferences.edit().putString("name", name).apply()
            }

            startActivity(intent)
        }
    }

    private fun showDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    override fun onDestroy() {
        super.onDestroy()
        val sharedPreferences = getSharedPreferences("userPref", MODE_PRIVATE)
        sharedPreferences.edit().remove("name").apply()
    }
}