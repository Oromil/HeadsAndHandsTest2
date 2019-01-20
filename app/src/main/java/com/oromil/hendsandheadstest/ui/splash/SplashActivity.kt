package com.oromil.hendsandheadstest.ui.splash

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.view.View
import com.oromil.hendsandheadstest.ui.base.BaseActivity
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.ui.auth.SignInActivity
import com.oromil.hendsandheadstest.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*
import java.security.AccessControlContext

class SplashActivity : BaseActivity<SplashViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun getViewModelClass(): Class<SplashViewModel> = SplashViewModel::class.java

    override fun initViews() {
        btnLogin.setOnClickListener {
            mViewModel.checkLoggedUser()
            progressBar.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.isUserLogged.observe(this, Observer { isLogged ->
            isLogged ?: return@Observer
            if (isLogged)
                MainActivity.start(this)
            else SignInActivity.start(this)
        })
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, SplashActivity::class.java))
    }
}