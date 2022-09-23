package com.example.storyapps.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.storyapps.R
import com.example.storyapps.databinding.ActivityLoginBinding
import com.example.storyapps.datasource.local.entity.LoginBody
import com.example.storyapps.datasource.local.entity.LoginModel
import com.example.storyapps.ui.home.HomeActivity
import com.example.storyapps.ui.register.RegisterActivity
import com.example.storyapps.ui.viewmodel.ViewModelFactory
import com.example.storyapps.utils.Utils.Companion.showLoading
import com.example.storyapps.vo.Status

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")

class LoginActivity : AppCompatActivity() {
    private lateinit var loginBinding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)

        loginViewModel = obtainViewModel(this)
        listener()
    }

    private fun listener() {
        with(loginBinding) {
            btLogin.setOnClickListener {
                if (edLoginEmail.text.isNullOrEmpty() || edLoginPassword.text.isNullOrEmpty()) {
                    if (edLoginEmail.text.isNullOrEmpty()) {
                        edLoginEmail.error = getString(R.string.all_email_empty)
                    }
                    if (edLoginPassword.text.isNullOrEmpty()) {
                        edLoginPassword.error = getString(R.string.all_password_empty)
                    }
                } else {
                    pbLogin.showLoading(true)
                    val loginBody =
                        LoginBody(edLoginEmail.text.toString(), edLoginPassword.text.toString())
                    loginViewModel.loginAccount(loginBody).observe(this@LoginActivity) {
                        when (it.status) {
                            Status.LOADING -> pbLogin.showLoading(false)
                            Status.SUCCESS -> {
                                pbLogin.showLoading(false)
                                if (it.data != null) {
                                    val login = LoginModel(true, it.data.token as String)
                                    loginViewModel.saveLoginStatus(login)
                                    val intent =
                                        Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            Status.ERROR -> {
                                pbLogin.showLoading(false)
                                if (!it.message.isNullOrBlank()) {
                                    Toast.makeText(
                                        this@LoginActivity, it.message, Toast.LENGTH_LONG
                                    ).show()
                                }
                                edLoginEmail.text?.clear()
                                edLoginPassword.text?.clear()
                            }
                        }
                    }
                }
            }
            btLoginRegister.setOnClickListener {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@LoginActivity,
                    Pair(ivLogin, getString(R.string.photo)),
                    Pair(tvLoginTitleEmail, getString(R.string.all_email)),
                    Pair(edLoginEmail, getString(R.string.all_email_ed)),
                    Pair(tvLoginTitlePassword, getString(R.string.all_password)),
                    Pair(edLoginPassword, getString(R.string.all_password_ed)),
                    Pair(btLoginRegister, getString(R.string.all_register))
                )
                startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    private fun obtainViewModel(
        activity: AppCompatActivity
    ): LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[LoginViewModel::class.java]
    }
}