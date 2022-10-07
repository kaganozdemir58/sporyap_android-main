package com.sporyap.sporyap.utils

import android.Manifest
import android.content.Context
import android.os.Build
import pub.devrel.easypermissions.EasyPermissions

object TrackingUtility {


    /*
    *  if (ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){
                cameraPermissionIntentResult.launch(Manifest.permission.CAMERA)
                return@setOnClickListener
            }

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P && ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.WRITE_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
                writeExternalPermissionIntentResult.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                return@setOnClickListener
            }
            if(ContextCompat.checkSelfPermission(requireContext() , Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
                readExternalPermissionIntentResult.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                return@setOnClickListener
            } fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.hasPermissions(context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }*/

    fun hasLocationPermissions(context: Context) =
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.hasPermissions(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }else{
            EasyPermissions.hasPermissions(context,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }
}