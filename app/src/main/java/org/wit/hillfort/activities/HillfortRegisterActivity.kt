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
import org.wit.hillfort.models.UserModel

class RegisterActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarAdd.title = "Register"
        setSupportActionBar(toolbarAdd)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        info("Hillfort Activity started..")

        app = application as MainApp
        var edit = false

        if (intent.hasExtra("hillfort_edit")) {
            edit = true
            toolbarAdd.title = "Edit Hillfort"
            setSupportActionBar(toolbarAdd)
            user = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            hillfortTitle.setText(user.title)
            description.setText(user.description)
            hillfortImage.setImageBitmap(readImageFromPath(this, user.image))
            hillfortImage.visibility = View.VISIBLE
            if (user.image != null) {
                chooseImage.setText(R.string.change_hillfortImage)
            }
            btnAdd.setText(R.string.button_saveHillfort)
        }

        btnAdd.setOnClickListener {
            user.title = hillfortTitle.text.toString()
            user.description = description.text.toString()
            if (user.title.isEmpty() or user.description.isEmpty()) {
                toast(R.string.hint_EnterHillfortTitle)
            } else {
                if (edit) {
                    app.hillforts.update(user.copy())
                } else {
                    app.hillforts.create(user.copy())
                }
                info("add Button Pressed: $hillfortTitle")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }

        btnDelete.setOnClickListener {
            alert(R.string.deletePrompt) {
                yesButton {
                    app.hillforts.delete(user)
                    finish()
                }
                noButton {}
            }.show()
        }

        chooseImage.setOnClickListener {
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
                    user.image = data.getData().toString()
                    hillfortImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortImage.visibility = View.VISIBLE
                    chooseImage.setText(R.string.change_hillfortImage)
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

