package com.example.tfg_geofamily.ui.map.adapters

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.example.tfg_geofamily.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class CustomInfoWindowForGoogleMap(context: Context) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.custom_marker_view, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val tvIcon = view.findViewById<ImageView>(R.id.iconMarker)

        val titleSplit = marker.title?.split("#")!!.toTypedArray()

        tvTitle.text = titleSplit[0]
        tvSnippet.text = marker.snippet

        var myUri: Uri = Uri.parse(titleSplit[1])
        Glide.with(mContext)
            .asBitmap()
            .load(myUri)
            .fitCenter()
            .centerCrop()
            .into(
                BitmapImageViewTarget(tvIcon)
            )

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}

