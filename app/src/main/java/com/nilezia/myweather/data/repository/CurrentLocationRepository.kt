package com.nilezia.myweather.data.repository

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resumeWithException


interface CurrentLocationRepository {
   suspend fun getCurrentLocation(): Location
}

class CurrentLocationRepositoryImpl @Inject constructor(private val fusedLocationProviderClient: FusedLocationProviderClient):CurrentLocationRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation():  Location = suspendCancellableCoroutine { cont ->
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) cont.resume(location) {}
                else cont.resumeWithException(Exception("Location not available"))
            }
            .addOnFailureListener { cont.resumeWithException(it) }

}}