package org.wit.hillfort.activities

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_all_hillforts_map.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortAllMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, AnkoLogger {

    lateinit var map: GoogleMap
    lateinit var app: MainApp
    var location = HillfortModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_hillforts_map)
        app = application as MainApp
        setSupportActionBar(toolbarAllHillforts)
        allHilllforts_Map.onCreate(savedInstanceState)
        allHilllforts_Map.getMapAsync {
            map = it
            configureMap()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        allHilllforts_Map.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        allHilllforts_Map.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        allHilllforts_Map.onPause()
    }

    override fun onResume() {
        super.onResume()
        allHilllforts_Map.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        allHilllforts_Map.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null) {

            var allHillforts = app.hillforts.findAll()
            val currentHill= allHillforts.find{ it.title == marker.title }
            if (currentHill != null) {
                cardAllHillfortsTitle.text = currentHill.title
                cardAllHillfortsDescription.text = currentHill.description
                cardAllHillfortsLocation.text = "Address: " + currentHill.address
                cardAllHillfortsImage.setImageBitmap(readImageFromPath(cardAllHillfortsImage.context, currentHill.firstImage))
                if (currentHill.visited) {
                    cardAllHillfortsIndicator.setBackgroundColor(Color.parseColor("#5db761"))
                    cardAllHillfortsIndicator.setText(R.string.isVisited)
                } else {
                    cardAllHillfortsIndicator.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    cardAllHillfortsIndicator.setText(R.string.notVisited)
                }

            }
            
        }
        return false
    }

    fun configureMap() {
        app.hillforts.findAll().forEach {
            val loc = LatLng(it.lat, it.lng)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
        map.setOnMarkerClickListener(this)
    }
}