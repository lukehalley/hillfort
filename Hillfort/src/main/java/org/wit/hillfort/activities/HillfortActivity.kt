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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class HillfortActivity : AppCompatActivity(), AnkoLogger {

    var hillfort = HillfortModel()
    lateinit var app: MainApp
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2
    val SECOND_IMAGE_REQUEST = 3
    val THIRD_IMAGE_REQUEST = 4
    val FOURTH_IMAGE_REQUEST = 5

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
            cardHillfortTitle.setText(hillfort.title)
            cardHillfortDescription.setText(hillfort.description)
            additionalNotes.setText(hillfort.addNotes)
            visitedSwitch.isChecked = hillfort.visited
            hillfortLocation.setText(R.string.button_changeLocation)
            deleteHillfortBtn.visibility = View.VISIBLE
            if (hillfort.visited) {
                dateVisited.text = hillfort.dateVisited
                dateVisited.visibility = View.VISIBLE
            }
            if (hillfort.firstImage.isNotEmpty()) {
                hillfortFirstImage.setImageBitmap(readImageFromPath(this, hillfort.firstImage))
                hillfortFirstImage.visibility = View.VISIBLE
                chooseFirstImage.setText(R.string.change_hillfortFirstImage)
            }
            if (hillfort.secondImage.isNotEmpty()) {
                hillfortSecondImage.setImageBitmap(readImageFromPath(this, hillfort.secondImage))
                hillfortSecondImage.visibility = View.VISIBLE
                chooseSecondImage.visibility = View.VISIBLE
                chooseSecondImage.setText(R.string.change_hillfortSecondImage)
            }
            if (hillfort.thirdImage.isNotEmpty()) {
                hillfortThirdImage.setImageBitmap(readImageFromPath(this, hillfort.thirdImage))
                hillfortThirdImage.visibility = View.VISIBLE
                chooseThirdImage.visibility = View.VISIBLE
                chooseThirdImage.setText(R.string.change_hillfortThirdImage)
            }
            if (hillfort.fourthImage.isNotEmpty()) {
                hillfortFourthImage.setImageBitmap(readImageFromPath(this, hillfort.fourthImage))
                hillfortFourthImage.visibility = View.VISIBLE
                chooseFourthImage.visibility = View.VISIBLE
                chooseFourthImage.setText(R.string.change_hillfortFourthImage)
            }
            addHillfortBtn.setText(R.string.button_saveHillfort)
        }

        addHillfortBtn.setOnClickListener {
            hillfort.title = cardHillfortTitle.text.toString()
            hillfort.description = cardHillfortDescription.text.toString()
            hillfort.addNotes = additionalNotes.text.toString()
            hillfort.visited = visitedSwitch.isChecked
            hillfort.dateVisited = dateVisited.text.toString()
            if (hillfort.title.isEmpty() or hillfort.description.isEmpty()) {
                toast(R.string.hint_EnterHillfortTitle)
            } else {
                if (edit) {
                    app.hillforts.update(hillfort.copy())
                } else {
                    app.hillforts.create(hillfort.copy())
                }
                info("add Button Pressed: $cardHillfortTitle")
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }
        }

        deleteHillfortBtn.setOnClickListener {
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

        chooseSecondImage.setOnClickListener {
            showImagePicker(this, SECOND_IMAGE_REQUEST)
        }

        chooseThirdImage.setOnClickListener {
            showImagePicker(this, THIRD_IMAGE_REQUEST)
        }

        chooseFourthImage.setOnClickListener {
            showImagePicker(this, FOURTH_IMAGE_REQUEST)
        }

        hillfortLocation.setOnClickListener {
            val location = HillfortModel(
                    hillfort.id,
                    hillfort.title,
                    hillfort.description,
                    hillfort.addNotes,
                    hillfort.visited,
                    hillfort.dateVisited,
                    52.245696,
                    -7.139102,
                    15f,
                    hillfort.address,
                    hillfort.firstImage,
                    hillfort.secondImage,
                    hillfort.thirdImage,
                    hillfort.fourthImage)
//            val location = HillfortModel(52.245696, -7.139102, 15f)
            if (location.zoom != 0f) {
                location.lat = hillfort.lat
                location.lng = hillfort.lng
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<HillfortMapsActivity>().putExtra("location", location), LOCATION_REQUEST)
        }

        visitedSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
                dateVisited.text = current.format(formatter).toString()
                dateVisited.visibility = View.VISIBLE
            } else {
                dateVisited.visibility = View.INVISIBLE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                alert(R.string.unsavedPrompt) {
                    yesButton {
                        finish()
                    }
                    noButton {}
                }.show()
            }
            R.id.item_deleteHillfort -> {
                alert(R.string.deletePrompt) {
                    yesButton {
                        app.hillforts.delete(hillfort)
                        finish()
                    }
                    noButton {}
                }.show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.firstImage = data.getData().toString()
                    hillfortFirstImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortFirstImage.visibility = View.VISIBLE
                    chooseFirstImage.setText(R.string.change_hillfortFirstImage)
                    chooseSecondImage.visibility = View.VISIBLE
                    chooseSecondImage.setText(R.string.button_addAnotherImage)
                }
            }
            SECOND_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.secondImage = data.getData().toString()
                    hillfortSecondImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortSecondImage.visibility = View.VISIBLE
                    chooseSecondImage.setText(R.string.change_hillfortSecondImage)
                    chooseThirdImage.visibility = View.VISIBLE
                    chooseThirdImage.setText(R.string.button_addAnotherImage)

                }
            }
            THIRD_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.thirdImage = data.getData().toString()
                    hillfortThirdImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortThirdImage.visibility = View.VISIBLE
                    chooseThirdImage.setText(R.string.change_hillfortThirdImage)
                    chooseFourthImage.visibility = View.VISIBLE
                    chooseFourthImage.setText(R.string.button_addAnotherImage)
                }
            }
            FOURTH_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.fourthImage = data.getData().toString()
                    hillfortFourthImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortFourthImage.visibility = View.VISIBLE
                    chooseFourthImage.setText(R.string.change_hillfortFourthImage)
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    val location = data.extras.getParcelable<HillfortModel>("location")
                    hillfort.lat = location.lat
                    hillfort.lng = location.lng
                    hillfort.zoom = location.zoom
                    hillfort.address = location.address
                    hillfortLocation.setText(R.string.button_changeLocation)
                }
            }
        }
    }

    override fun onBackPressed() {
        alert(R.string.unsavedPrompt) {
            yesButton {
                finish()
                super.onBackPressed()
            }
            noButton {}
        }.show()
    }

//    override fun onNavigateUp() {
//        alert(R.string.unsavedPrompt) {
//            yesButton {
//                finish()
//                super.onNavigateUp()
//            }
//            noButton {}
//        }.show()
//    }

}
