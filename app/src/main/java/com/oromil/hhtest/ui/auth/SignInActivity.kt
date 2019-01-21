package com.oromil.hhtest.ui.auth

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.oromil.hhtest.R
import com.oromil.hhtest.data.entities.UserAccount
import com.oromil.hhtest.ui.base.BaseActivity
import com.oromil.hhtest.ui.main.MainActivity
import com.oromil.hhtest.ui.registration.RESULT_INTENT_KEY
import com.oromil.hhtest.ui.registration.RegistrationActivity
import com.oromil.hhtest.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.app_bar.*

const val REGISTRATION_REQUEST_CODE = 1

class SignInActivity : BaseActivity<SignInViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_signin

    override fun getViewModelClass(): Class<SignInViewModel> = SignInViewModel::class.java

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_signin_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_register -> startActivityForResult(Intent(this,
                    RegistrationActivity::class.java), REGISTRATION_REQUEST_CODE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        SplashActivity.start(this)
        finish()
        return true
    }

    override fun initViews() {
        setupActionBar()
        btnApply.setOnClickListener {
            emailInputLayout.error = ""
            mViewModel.signIn(etEmail.text.toString(), etPassword.text.toString())
        }
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.authorizationSuccess.observe(this, Observer { success ->
            success ?: return@Observer
            if (success) {
                MainActivity.start(this)
                finish()
            } else {
                etPassword.text.clear()
                emailInputLayout.error = getString(R.string.failure_login)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REGISTRATION_REQUEST_CODE -> when (resultCode) {
                Activity.RESULT_OK -> {
                    data ?: return
                    mViewModel.login(data.extras.getSerializable(RESULT_INTENT_KEY) as UserAccount)
                }
            }
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.title_authorization)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, SignInActivity::class.java))
    }
}