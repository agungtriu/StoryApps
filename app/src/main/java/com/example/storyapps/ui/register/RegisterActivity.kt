package com.example.storyapps.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityRegisterBinding
import com.example.storyapps.datasource.local.entity.RegisterBody
import com.example.storyapps.ui.login.LoginActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.showLoading
import com.example.storyapps.vo.Status

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerBinding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(registerBinding.root)
        registerViewModel = obtainViewModel(this)
        registerListener()
    }

    private fun registerListener() {
        with(registerBinding) {
            btRegister.setOnClickListener {
                if (edRegisterName.text.isNullOrEmpty() || edRegisterEmail.text.isNullOrEmpty() || edRegisterPassword.text.isNullOrEmpty()) {
                    if (edRegisterName.text.isNullOrEmpty()) {
                        edRegisterName.error = getString(R.string.all_name_empty)
                    }
                    if (edRegisterEmail.text.isNullOrEmpty()) {
                        edRegisterEmail.error = getString(R.string.all_email_empty)
                    }
                    if (edRegisterPassword.text.isNullOrEmpty()) {
                        edRegisterPassword.error = getString(R.string.all_password_empty)
                    }
                } else {
                    pbRegister.showLoading(true)
                    val registerBody = RegisterBody(
                        edRegisterName.text.toString(),
                        edRegisterEmail.text.toString(),
                        edRegisterPassword.text.toString()
                    )
                    registerViewModel.registerAccount(registerBody).observe(this@RegisterActivity) {
                        when (it.status) {
                            Status.LOADING -> pbRegister.showLoading(false)
                            Status.SUCCESS -> {
                                pbRegister.showLoading(false)
                                Toast.makeText(
                                    this@RegisterActivity, it.data?.message, Toast.LENGTH_SHORT
                                ).show()
                                val intent =
                                    Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            Status.ERROR -> {
                                pbRegister.showLoading(false)
                                if (!it.data?.message.isNullOrEmpty()) {
                                    Toast.makeText(
                                        this@RegisterActivity, it.data?.message, Toast.LENGTH_SHORT
                                    ).show()
                                } else if (!it.message.isNullOrEmpty()) {
                                    Toast.makeText(
                                        this@RegisterActivity, it.message, Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[RegisterViewModel::class.java]
    }
}