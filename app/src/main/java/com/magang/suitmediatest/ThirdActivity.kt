package com.magang.suitmediatest

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class ThirdActivity : AppCompatActivity() {

    private lateinit var userAdapter: UserAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar // Add a reference to the progress bar
    private val thirdViewModel: ThirdViewModel by viewModels()

    private var currentPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_third)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbarTitle = findViewById<TextView>(R.id.toolbar_title)
        toolbarTitle.text = getString(R.string.third_screen)
        val backButton = findViewById<ImageView>(R.id.back)
        backButton.setOnClickListener {
            @Suppress("DEPRECATION")
            onBackPressed()
        }



        userAdapter = UserAdapter { selectedUser ->
            val intent = Intent()
            val fullName = "${selectedUser.firstName} ${selectedUser.lastName}"
            intent.putExtra("selected_user_name", fullName)
            setResult(RESULT_OK, intent)
            val sharedPreferences = getSharedPreferences("userPref", MODE_PRIVATE)
            sharedPreferences.edit().putString("usernameselected", fullName).apply()
            finish()
        }
        val recyclerView = findViewById<RecyclerView>(R.id.rv_user)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            currentPage = 1
            thirdViewModel.refreshUsers(currentPage)
        }

        progressBar = findViewById(R.id.progressBarFollow)

        thirdViewModel.users.observe(this) {
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
            userAdapter.submitList(it.toList())
        }

        thirdViewModel.error.observe(this) {
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        thirdViewModel.loading.observe(this) { isLoading ->
            if (isLoading) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        thirdViewModel.loadUsers(currentPage)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (layoutManager.findLastCompletelyVisibleItemPosition() == userAdapter.itemCount - 1) {
                    currentPage++
                    thirdViewModel.loadUsers(currentPage)
                }
            }
        })
    }
}