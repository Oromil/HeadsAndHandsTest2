package com.oromil.hhtest.ui.main

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.google.android.gms.common.api.Status
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
//import com.afollestad.materialdialogs.MaterialDialog
import com.oromil.hhtest.R
import com.oromil.hhtest.data.entities.StoryEntity
import com.oromil.hhtest.ui.auth.SignInActivity
import com.oromil.hhtest.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*

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
            R.id.logout -> showLogOutDialog()
            R.id.forecast -> mViewModel.getLocation()
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
        mViewModel.news.observe(this, Observer { data -> processNewsResponse(data) })
        mViewModel.logout.observe(this, Observer { processLogout() })

        mViewModel.weather.observe(this, Observer { message ->
            processWeatherResponse(message)
        })

        mViewModel.requestPermissions().observe(this, Observer { listPermissionsNeeded ->
            processPermissionsRequest(listPermissionsNeeded)
        })

        mViewModel.requestEnable().observe(this, Observer { status ->
            processGeolocationEnablingRequest(status)
        })

        mViewModel.loadingError.observe(this, Observer { processLoadingError() })
    }

    private fun processLoadingError() {
        Toast.makeText(this, getString(R.string.network_error), Toast.LENGTH_SHORT).show()
    }

    private fun processPermissionsRequest(listPermissionsNeeded: List<String>?) {
        listPermissionsNeeded ?: return
        ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                GEOLOCATION_PERMISSIONS_REQUEST)
    }

    private fun processGeolocationEnablingRequest(status: Status?) {
        status ?: return
        status.startResolutionForResult(this@MainActivity, GEOLOCATION_ENABLING_REQUEST)
    }

    private fun processWeatherResponse(message: String?) {
        if (message == null) {
            showSnackBar(getString(R.string.snackbar_error_message))
            return
        }
        showSnackBar(message)
    }

    private fun processNewsResponse(data: List<StoryEntity>?) {
        if (data != null) {
            (newsRecyclerView.adapter as NewsAdapter).updateData(data)
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
        }
    }

    private fun processLogout() {
        SignInActivity.start(this)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        val permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            mViewModel.getLocation()
        } else {
            showSnackBar(getString(R.string.snackbar_geolocation_error))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            GEOLOCATION_ENABLING_REQUEST -> when (resultCode) {
                Activity.RESULT_OK -> {
                    mViewModel.getLocation()
                }
                Activity.RESULT_CANCELED -> showSnackBar(getString(R.string.snackbar_geolocation_error))
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(root, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(R.string.dialog_action_ok)) { }
                .show()
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.setTitle(R.string.title_news)
    }

    private fun showLogOutDialog() {
        MaterialDialog.Builder(this)
                .title(R.string.exit_dialog_title)
                .content(R.string.exit_dialog_message)
                .positiveText(R.string.dialog_action_ok)
                .onPositive { _, _ -> mViewModel.logoutUser() }
                .negativeText(R.string.dialog_action_cancel)
                .show()
    }

    companion object {
        private const val GEOLOCATION_PERMISSIONS_REQUEST = 60
        private const val GEOLOCATION_ENABLING_REQUEST = 56
        fun start(context: Context) = context
                .startActivity(Intent(context, MainActivity::class.java))
    }
}
