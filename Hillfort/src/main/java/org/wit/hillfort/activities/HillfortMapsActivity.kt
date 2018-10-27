package org.wit.hillfort.activities

import android.app.Activity
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.models.Location

class HillfortMapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener, GoogleMap.OnCameraMoveListener, AnkoLogger {
    private lateinit var map: GoogleMap
    var location = Location()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        location = intent.extras.getParcelable<Location>("location")
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMarkerDragListener(this)
        map.setOnCameraMoveListener((this))
        val loc = LatLng(location.lat, location.lng)
        val options = MarkerOptions()
                .title("Hillfort")
                .snippet("GPS : " + loc.toString())
                .draggable(true)
                .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    override fun onMarkerDrag(marker: Marker) {
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onCameraMove() {
        val zoom = map.cameraPosition.zoom
        location.zoom = zoom
    }

    override fun onMarkerDragEnd(marker: Marker) {
        val geocoder = Geocoder(this)
        location.lat = marker.position.latitude
        location.lng = marker.position.longitude
        location.zoom = map.cameraPosition.zoom
        val loc = LatLng(marker.position.latitude, marker.position.longitude)
        val addresses = geocoder.getFromLocation(location.lat, location.lng, 1)
        val addy = addresses.get(0).getAddressLine(0)
        info { "ADDRESS: " + addy }
        marker.snippet = "GPS : " + loc.toString()
    }

    override fun onBackPressed() {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }

//    private fun getAddress(latLng: LatLng): String {
//        val geocoder = Geocoder(this)
//        val addresses: List<Address>?
//        val address: Address?
//        var addressText = ""
//
//        try {
//            // 2
//            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//            // 3
//            if (null != addresses && !addresses.isEmpty()) {
//                address = addresses[0]
//                for (i in 0 until address.maxAddressLineIndex) {
//                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
//                }
//            }
//        } catch (e: IOException) {
//            info { "MapsActivity" + e.localizedMessage }
//        }
//
//        return addressText
//    }
}