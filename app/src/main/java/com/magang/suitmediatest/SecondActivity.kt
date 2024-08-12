package com.magang.suitmediatest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {
    private lateinit var usernameselected: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        usernameselected = findViewById(R.id.usernameselected)

        val backButton = findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }

        val name = intent.getStringExtra("name") ?: getSharedPreferences("userPref", MODE_PRIVATE).getString("name", "Username")
        findViewById<TextView>(R.id.name).text = name

        val nextButton = findViewById<Button>(R.id.nextButton)
        nextButton.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            @Suppress("DEPRECATION")
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            val selectedUserName = data?.getStringExtra("selected_user_name")
            usernameselected.text = selectedUserName ?: "No user selected"
        }
    }

    companion object {
        const val REQUEST_CODE = 100
    }
}