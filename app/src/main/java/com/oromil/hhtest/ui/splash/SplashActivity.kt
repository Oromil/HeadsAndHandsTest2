package com.oromil.hhtest.ui.splash

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.view.View
import com.oromil.hhtest.ui.base.BaseActivity
import com.oromil.hhtest.R
import com.oromil.hhtest.ui.auth.SignInActivity
import com.oromil.hhtest.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*

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
        progressBar.visibility = View.INVISIBLE
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.isUserLogged.observe(this, Observer { isLogged ->
            processUserLoggedResponse(isLogged)
        })
    }

    private fun processUserLoggedResponse(isLogged: Boolean?) {
        isLogged ?: return
        if (isLogged) {
            MainActivity.start(this)
            finish()
        } else SignInActivity.start(this)
    }

    companion object {
        fun start(context: Context) = context.startActivity(Intent(context, SplashActivity::class.java))
    }
}