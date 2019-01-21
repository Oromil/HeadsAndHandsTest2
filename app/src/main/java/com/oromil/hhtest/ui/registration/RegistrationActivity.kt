package com.oromil.hhtest.ui.registration

import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.design.widget.TextInputLayout
import android.view.View
import com.oromil.hhtest.R
import com.oromil.hhtest.data.entities.UserAccount
import com.oromil.hhtest.ui.base.BaseActivity
import com.oromil.hhtest.ui.registration.RegistrationViewModel.InputError.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.app_bar.*

const val RESULT_INTENT_KEY = "user_account"

class RegistrationActivity : BaseActivity<RegistrationViewModel>() {

    lateinit var inputLayouts: ArrayList<TextInputLayout>

    override fun getLayoutId(): Int = R.layout.activity_registration

    override fun getViewModelClass(): Class<RegistrationViewModel> = RegistrationViewModel::class.java

    override fun initViews() {
        setupActionBar()
        inputLayouts = arrayListOf(emailInputLayout, nameTextInputLayout, passwordInputLayout, repeatInputLayout)
        btnApply.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            updateInputMessages()
            mViewModel.saveUser(etEmail.text.toString(),
                    etName.text.toString(), etPassword.text.toString(), etRepeatPassword.text.toString())
        }
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.incorrectInput.observe(this, Observer { error ->
            progressBar.visibility = View.GONE
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
            finishWithResult(account)
        })
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.title_registration)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun updateInputMessages() {
        for (inputLayout in inputLayouts)
            inputLayout.error = ""
    }

    private fun finishWithResult(userAccount: UserAccount) {
        val resultIntent = Intent()
        resultIntent.putExtra(RESULT_INTENT_KEY, userAccount)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}