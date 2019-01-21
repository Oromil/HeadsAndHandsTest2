package com.oromil.hhtest.data

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationSettingsStatusCodes.*
import javax.inject.Inject

class GeolocationProvider @Inject constructor(private val context: Context) :
        GoogleApiClient.ConnectionCallbacks {

    val requestPermissions = MutableLiveData<List<String>>()
    val requestGeolocationEnable = MutableLiveData<Status>()
    val locationData = MutableLiveData<Location>()

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(newLocation: LocationResult) {
            locationData.value = newLocation.lastLocation
        }
    }

    private val googleApiClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addApi(LocationServices.API)
            .build()

    init {
        connectGoogleApiClient()
    }

    override fun onConnected(bundle: Bundle?) {
        updateGeolocation()
    }

    override fun onConnectionSuspended(cause: Int) {
        Log.e("GoogleApiException", "GoogleApiClient was suspended. Cause: $cause")
    }

    fun updateGeolocation() {
        if (!checkGoogleApiClientConnected()) {
            connectGoogleApiClient()
            return
        }
        if (!checkPermissions()) {
            requestPermissions.value = arrayListOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }
        val locationRequest = createLocationRequest()
        checkGeolocationAvailable(createLocationSettingsRequest(locationRequest))
        requestLocationUpdates(locationRequest)
    }

    private fun checkGoogleApiClientConnected() = googleApiClient.isConnected

    private fun connectGoogleApiClient() = googleApiClient.connect()

    private fun checkPermissions(): Boolean = ContextCompat.checkSelfPermission(context,
            android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun createLocationSettingsRequest(request: LocationRequest): LocationSettingsRequest {
        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(request)
        builder.setAlwaysShow(true)
        return builder.build()
    }

    private fun createLocationRequest(): LocationRequest {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.numUpdates = 1
        return locationRequest
    }

    private fun checkGeolocationAvailable(settingsRequest: LocationSettingsRequest) {
        val pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, settingsRequest)
        pendingResult.setResultCallback { result ->
            val status = result.status
            when (status.statusCode) {
                RESOLUTION_REQUIRED -> requestGeolocationEnable.value = status
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(locationRequest: LocationRequest) {
        val client = FusedLocationProviderClient(context)
        client.requestLocationUpdates(locationRequest, locationCallback, null)
    }
}