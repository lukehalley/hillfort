package org.wit.hillfort.activities

import android.content.Intent
import android.location.Location
import org.jetbrains.anko.intentFor
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortPresenter(val activity: HillfortActivity) {

    var hillfort = HillfortModel()
    lateinit var app: MainApp

    val LOCATION_REQUEST = 1
    val FIRST_GALLERY_IMAGE_REQUEST = 2
    val SECOND_GALLERY_IMAGE_REQUEST = 3
    val THIRD_GALLERY_IMAGE_REQUEST = 4
    val FOURTH_GALLERY_IMAGE_REQUEST = 5

    val FIRST_CAMERA_IMAGE_REQUEST = 6
    val SECOND_CAMERA_IMAGE_REQUEST = 7
    val THIRD_CAMERA_IMAGE_REQUEST = 8
    val FOURTH_CAMERA_IMAGE_REQUEST = 9

    var mCurrentPhotoPath: String = ""
    var edit = false;

    init {
        app = activity.application as MainApp
        if (activity.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = activity.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            activity.showHillfort(hillfort)
        }
    }

    fun doAddOrSave(title: String, description: String) {
        hillfort.title = title
        hillfort.description = description
        if (edit) {
            app.hillforts.update(hillfort)
        } else {
            app.hillforts.create(hillfort)
        }
        activity.finish()
    }

    fun doCancel() {
        activity.finish()
    }

    fun doDelete() {
        app.hillforts.delete(hillfort)
        activity.finish()
    }

    fun doSelectImage() {
        showImagePicker(activity, IMAGE_REQUEST)
    }

    fun doSetLocation() {
        if (hillfort.zoom != 0f) {
            location.lat = hillfort.lat
            location.lng = hillfort.lng
            location.zoom = hillfort.zoom
        }
        activity.startActivityForResult(activity.intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        when (requestCode) {
            IMAGE_REQUEST -> {
                hillfort.image = data.data.toString()
                activity.showPlacemark(hillfort)
            }
            LOCATION_REQUEST -> {
                location = data.extras.getParcelable<Location>("location")
                hillfort.lat = location.lat
                hillfort.lng = location.lng
                hillfort.zoom = location.zoom
            }
        }
    }

}