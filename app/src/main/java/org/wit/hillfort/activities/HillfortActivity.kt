package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImage
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location

class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    var location = Location(52.245696, -7.139102, 15f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        info("Hillfort Activity started..")

        app = application as MainApp
        var edit = false

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            toolbarAdd.title = "Edit Hillfort"
            setSupportActionBar(toolbarAdd)
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            hillfortTitle.setText(hillfort.title)
            description.setText(hillfort.description)
            hillfortFirstImage.setImageBitmap(readImageFromPath(this, hillfort.image))
            hillfortFirstImage.visibility = View.VISIBLE
            if (hillfort.image != null) {
                chooseFirstImage.setText(R.string.change_hillfortImage)
            }
            btnAdd.setText(R.string.button_saveHillfort)
        }

        btnAdd.setOnClickListener {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = description.text.toString()
            if (hillfort.title.isEmpty() or hillfort.description.isEmpty()) {
                toast(R.string.hint_EnterHillfortTitle)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }
                info("add Button Pressed: $hillfortTitle")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }

        btnDelete.setOnClickListener {
            alert(R.string.deletePrompt) {
                yesButton {
                    app.hillforts.delete(hillfort)
                    finish()
                }
                noButton {}
            }.show()
        }

        chooseFirstImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.image = data.getData().toString()
                    hillfortFirstImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortFirstImage.visibility = View.VISIBLE
                    chooseFirstImage.setText(R.string.change_hillfortImage)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    location = data.extras.getParcelable<Location>("location")
                }
            }
        }
    }
}

