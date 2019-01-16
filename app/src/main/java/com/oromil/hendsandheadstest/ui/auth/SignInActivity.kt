package com.oromil.hendsandheadstest.ui.auth

import android.arch.lifecycle.Observer
import android.view.Menu
import android.view.MenuItem
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.ui.base.BaseActivity
import com.oromil.hendsandheadstest.ui.main.MainActivity
import com.oromil.hendsandheadstest.ui.registration.RegistrationActivity
import kotlinx.android.synthetic.main.app_bar.*

class SignInActivity : BaseActivity<SignInViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_signin

    override fun getViewModelClass(): Class<SignInViewModel> = SignInViewModel::class.java

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_signin_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_register -> RegistrationActivity.start(this)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun initViews() {
        setupActionBar()
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.authorizationSuccess.observe(this, Observer {
            MainActivity.start(this)
        })
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.authorization)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
    }
}