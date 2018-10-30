package org.wit.hillfort.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
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
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class HillfortActivity : AppCompatActivity(), AnkoLogger {

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
                chooseFirstImageGallery.visibility = View.VISIBLE
                chooseFirstImageCamera.visibility = View.VISIBLE
            }
            if (hillfort.secondImage.isNotEmpty()) {
                hillfortSecondImage.setImageBitmap(readImageFromPath(this, hillfort.secondImage))
                hillfortSecondImage.visibility = View.VISIBLE
                chooseSecondImageGallery.visibility = View.VISIBLE
                chooseSecondImageCamera.visibility = View.VISIBLE
            }
            if (hillfort.thirdImage.isNotEmpty()) {
                hillfortThirdImage.setImageBitmap(readImageFromPath(this, hillfort.thirdImage))
                hillfortThirdImage.visibility = View.VISIBLE
                chooseThirdImageGallery.visibility = View.VISIBLE
                chooseThirdImageCamera.visibility = View.VISIBLE
            }
            if (hillfort.fourthImage.isNotEmpty()) {
                hillfortFourthImage.setImageBitmap(readImageFromPath(this, hillfort.fourthImage))
                hillfortFourthImage.visibility = View.VISIBLE
                chooseFourthImageGallery.visibility = View.VISIBLE
                chooseFourthImageCamera.visibility = View.VISIBLE
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

        // Gallery
        chooseFirstImageGallery.setOnClickListener {
            showImagePicker(this, FIRST_GALLERY_IMAGE_REQUEST)
        }

        chooseSecondImageGallery.setOnClickListener {
            showImagePicker(this, SECOND_GALLERY_IMAGE_REQUEST)
        }

        chooseThirdImageGallery.setOnClickListener {
            showImagePicker(this, THIRD_GALLERY_IMAGE_REQUEST)
        }

        chooseFourthImageGallery.setOnClickListener {
            showImagePicker(this, FOURTH_GALLERY_IMAGE_REQUEST)
        }

        // Camera
        chooseFirstImageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "org.wit.hillfort.fileprovider", it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, FIRST_CAMERA_IMAGE_REQUEST)
                    }
                }
            }
        }

        chooseSecondImageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "org.wit.hillfort.fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, SECOND_CAMERA_IMAGE_REQUEST)
                    }
                }
            }
        }

        chooseThirdImageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "org.wit.hillfort.fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, THIRD_CAMERA_IMAGE_REQUEST)
                    }
                }
            }
        }

        chooseFourthImageCamera.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                // Ensure that there's a camera activity to handle the intent
                takePictureIntent.resolveActivity(packageManager)?.also {
                    // Create the File where the photo should go
                    val photoFile: File? = try {
                        createImageFile()
                    } catch (ex: IOException) {
                        // Error occurred while creating the File
                        null
                    }
                    // Continue only if the File was successfully created
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(
                                this,
                                "org.wit.hillfort.fileprovider",
                                it
                        )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, FOURTH_CAMERA_IMAGE_REQUEST)
                    }
                }
            }
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
            // GALLERY
            FIRST_GALLERY_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.firstImage = data.getData().toString()
                    hillfortFirstImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortFirstImage.visibility = View.VISIBLE
                    chooseFirstImageCamera.isClickable = false
                    chooseFirstImageCamera.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseSecondImageGallery.visibility = View.VISIBLE
                    chooseSecondImageCamera.visibility = View.VISIBLE
                }
            }
            SECOND_GALLERY_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.secondImage = data.getData().toString()
                    hillfortSecondImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortSecondImage.visibility = View.VISIBLE
                    chooseSecondImageCamera.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseSecondImageCamera.isClickable = false
                    chooseThirdImageGallery.visibility = View.VISIBLE
                    chooseThirdImageCamera.visibility = View.VISIBLE
                }
            }
            THIRD_GALLERY_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.thirdImage = data.getData().toString()
                    hillfortThirdImage.setImageBitmap(readImage(this, resultCode, data))
                    hillfortThirdImage.visibility = View.VISIBLE
                    chooseThirdImageCamera.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseThirdImageCamera.isClickable = false
                    chooseFourthImageGallery.visibility = View.VISIBLE
                    chooseFourthImageCamera.visibility = View.VISIBLE
                }
            }
            FOURTH_GALLERY_IMAGE_REQUEST -> {
                if (data != null) {
                    hillfort.fourthImage = data.getData().toString()
                    hillfortFourthImage.setImageBitmap(readImage(this, resultCode, data))
                    chooseFourthImageCamera.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseFourthImageCamera.isClickable = false
                    hillfortFourthImage.visibility = View.VISIBLE
                }
            }

            // CAMERA
            FIRST_CAMERA_IMAGE_REQUEST -> {
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        val uri = intent.data
                        val bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        hillfort.firstImage = bitmap.toString()
                        hillfortFirstImage.setImageBitmap(decodeBitmap())
                    }
                    chooseFirstImageGallery.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseFirstImageGallery.isClickable = false
                    hillfortFirstImage.visibility = View.VISIBLE
                    chooseSecondImageGallery.visibility = View.VISIBLE
                    chooseSecondImageCamera.visibility = View.VISIBLE
                }
            }
            SECOND_CAMERA_IMAGE_REQUEST -> {
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        hillfort.secondImage = mCurrentPhotoPath
                        hillfortSecondImage.setImageBitmap(decodeBitmap())
                    }
                    chooseSecondImageGallery.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseSecondImageGallery.isClickable = false
                    hillfortSecondImage.visibility = View.VISIBLE
                    chooseThirdImageGallery.visibility = View.VISIBLE
                    chooseThirdImageCamera.visibility = View.VISIBLE
                }
            }
            THIRD_CAMERA_IMAGE_REQUEST -> {
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        hillfort.thirdImage = mCurrentPhotoPath
                        hillfortThirdImage.setImageBitmap(decodeBitmap())
                    }
                    chooseThirdImageGallery.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseThirdImageGallery.isClickable = false
                    hillfortThirdImage.visibility = View.VISIBLE
                    chooseFourthImageGallery.visibility = View.VISIBLE
                    chooseFourthImageCamera.visibility = View.VISIBLE
                }
            }
            FOURTH_CAMERA_IMAGE_REQUEST -> {
                if (data != null) {
                    if (resultCode == Activity.RESULT_OK) {
                        hillfortFourthImage.setImageBitmap(decodeBitmap())
                    }
                    chooseFourthImageGallery.setBackgroundColor(Color.parseColor("#FF9E9E9E"))
                    chooseFourthImageGallery.isClickable = false
                    hillfortFourthImage.visibility = View.VISIBLE
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

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            mCurrentPhotoPath = absolutePath
        }
    }

    fun decodeBitmap(): Bitmap {

        return BitmapFactory.decodeFile(mCurrentPhotoPath)

    }

}

