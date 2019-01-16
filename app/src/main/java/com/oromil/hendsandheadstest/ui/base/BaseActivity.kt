package com.oromil.hendsandheadstest.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

abstract class BaseActivity<M : ViewModel>
    : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    protected lateinit var mViewModel: M

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(getLayoutId())

        AndroidInjection.inject(this)
        mViewModel = ViewModelProviders.of(this, viewModelFactory)[getViewModelClass()]

        subscribeOnViewModelLiveData()

        initViews()
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun getViewModelClass(): Class<M>

    protected open fun initViews() {}

    protected abstract fun subscribeOnViewModelLiveData()
}