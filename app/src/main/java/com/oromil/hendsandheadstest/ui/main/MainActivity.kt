package com.oromil.hendsandheadstest.ui.main

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.oromil.hendsandheadstest.R
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.ui.auth.SignInActivity
import com.oromil.hendsandheadstest.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*

private const val GEOLOCATION_PERMISSIONS_REQUEST = 60
private const val GEOLOCATION_ENABLING_REQUEST = 56

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
        setupActionBar()

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

        mViewModel.weather.observe(this, Observer { message ->
            message ?: return@Observer
            showSnackBar(message)
        })

        mViewModel.requestPermissions().observe(this, Observer { listPermissionsNeeded ->
            listPermissionsNeeded ?: return@Observer
            ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    GEOLOCATION_PERMISSIONS_REQUEST)
        })

        mViewModel.requestEnable().observe(this, Observer { status ->
            status ?: return@Observer
            status.startResolutionForResult(this@MainActivity, GEOLOCATION_ENABLING_REQUEST)
        })

        mViewModel.loadingError.observe(this, Observer {
            Toast.makeText(this, "networkError", Toast.LENGTH_SHORT).show()
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            mViewModel.getLocation()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            GEOLOCATION_ENABLING_REQUEST -> when (resultCode) {
                Activity.RESULT_OK -> {
                    mViewModel.getLocation()
                }
                Activity.RESULT_CANCELED -> showSnackBar("")
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction("OK") { }
                .show()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.title_news)
    }

    companion object {
        fun start(context: Context) = context
                .startActivity(Intent(context, MainActivity::class.java))
    }
}
