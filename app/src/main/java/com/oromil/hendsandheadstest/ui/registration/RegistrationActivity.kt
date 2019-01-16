package com.oromil.hendsandheadstest.ui.registration

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.design.widget.TextInputLayout
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.ui.base.BaseActivity
import com.oromil.hendsandheadstest.ui.main.MainActivity
import com.oromil.hendsandheadstest.ui.registration.RegistrationViewModel.InputError.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.app_bar.*

class RegistrationActivity : BaseActivity<RegistrationViewModel>() {

    lateinit var inputLayouts: ArrayList<TextInputLayout>

    override fun getLayoutId(): Int = R.layout.activity_registration

    override fun getViewModelClass(): Class<RegistrationViewModel> = RegistrationViewModel::class.java

    override fun initViews() {
        setupActionBar()
        inputLayouts = arrayListOf(emailInputLayout, nameTextInputLayout, passwordInputLayout, repeatInputLayout)
        btnApply.setOnClickListener {
            updateInputMessages()
            //todo
            mViewModel.saveUser(etEmail.text.toString(),
                    etName.text.toString(), etPassword.text.toString(), etRepeatPassword.text.toString())
        }
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.incorrectInput.observe(this, Observer { error ->
            error ?: return@Observer
            when (error) {
                INCORRECT_EMAIL -> emailInputLayout.error = getString(R.string.email_error)
                INCORRECT_PASSWORD -> passwordInputLayout.error = getString(R.string.password_error)
                INCORRECT_NAME -> nameTextInputLayout.error = getString(R.string.name_error)
                INCORRECT_REPEAT -> repeatInputLayout.error = getString(R.string.repeat_password_error)
                EMAIL_EXISTS -> emailInputLayout.error = getString(R.string.user_exists_error)
            }
        })
        mViewModel.accountCreated.observe(this, Observer { account ->
            account ?: return@Observer
            mViewModel.loginUser(account)
            MainActivity.start(this)
        })
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.registration)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun updateInputMessages() {
        for (inputLayout in inputLayouts) {
            inputLayout.error = ""
        }
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, RegistrationActivity::class.java))
    }
}