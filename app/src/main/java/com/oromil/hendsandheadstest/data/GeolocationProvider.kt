package com.oromil.hendsandheadstest.data

import android.Manifest
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.*
import java.util.*
import javax.inject.Inject

class GeolocationProvider @Inject constructor(private val context: Context)
    :
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
{

    val requestPermissions = MutableLiveData<List<String>>()
    val requestGeolocationEnable = MutableLiveData<Status>()
    val locationData = MutableLiveData<Location>()

//    private val locationCallback = object :LocationCallback(){
//        override fun onLocationResult(p0: LocationResult?) {
//            locationData.value
//        }
//    }

    private val googleApiClient = GoogleApiClient.Builder(context)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build()

    init {
        googleApiClient.connect()
    }

    override fun onConnected(p0: Bundle?) {
        checkPermissions()
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    override fun onLocationChanged(newLocation: Location) {
//        locationData.value = newLocation
    }

    private fun checkPermissions() {
        val permissionLocation = ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
        val listPermissionsNeeded = ArrayList<String>()
        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
            if (!listPermissionsNeeded.isEmpty()) {
                requestPermissions.value = listPermissionsNeeded
            }
        } else {
            getMyLocation()
        }
    }

    fun getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected) {
                val permissionLocation = ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    val locationRequest = LocationRequest()
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationRequest.numUpdates = 1
                    val builder = LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest)
                    builder.setAlwaysShow(true)
                    val client = FusedLocationProviderClient(context)
                    client.requestLocationUpdates(locationRequest, object : LocationCallback(){
                        override fun onLocationResult(p0: LocationResult?) {
                            locationData.value = p0!!.lastLocation
                        }
                    }, null)
//                    LocationServices.FusedLocationApi
//                            .requestLocationUpdates(googleApiClient, locationRequest, this)
                    val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
                    result.setResultCallback { result ->
                        val status = result.status
                        when (status.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> requestGeolocationEnable.value = status
                        }
                    }
                }
            }
        }
    }
}