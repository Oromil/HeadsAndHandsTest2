package com.oromil.hendsandheadstest.ui.main

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.ui.auth.SignInActivity
import com.oromil.hendsandheadstest.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<MainViewModel>() {
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun getViewModelClass(): Class<MainViewModel> = MainViewModel::class.java

    override fun getLayoutId(): Int = R.layout.activity_main

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> mViewModel.logoutUser()
        }
        return true
    }

    override fun initViews() {
        swipeRefreshLayout = refreshLayout

        swipeRefreshLayout.setOnRefreshListener { mViewModel.update() }

        newsRecyclerView.layoutManager = LinearLayoutManager(this)
        newsRecyclerView.adapter = NewsAdapter()

        newsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                if (dy < 0) {
                    btn_top.visibility = View.VISIBLE
                } else btn_top.visibility = View.GONE
            }
        })

        btn_top.setOnClickListener { newsRecyclerView.scrollToPosition(0) }

        mViewModel.loadWether()
    }

    override fun subscribeOnViewModelLiveData() {
        mViewModel.update()
        mViewModel.result.observe(this, Observer { data ->
            (newsRecyclerView.adapter as NewsAdapter).updateData(data as List<StoryEntity>)
            swipeRefreshLayout.isRefreshing = false
        })
        mViewModel.logout.observe(this, Observer {
            SignInActivity.start(this)
            finish()
        })

        mViewModel.weather.observe(this, Observer {message ->
            message?:return@Observer
            showSnackBar(message)
        })
    }

    fun showSnackBar(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { }
                .show()
    }

    companion object {
        fun start(context: Context) = context
                .startActivity(Intent(context, MainActivity::class.java))
    }
}
