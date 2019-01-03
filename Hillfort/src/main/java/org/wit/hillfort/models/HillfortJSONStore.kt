package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.exists
import org.wit.hillfort.helpers.read
import org.wit.hillfort.helpers.write
import java.util.*

val HILLFORT_JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomHillfortId(): Long {
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger {

    val context: Context
    var hillforts = mutableListOf<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, HILLFORT_JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        hillfort.id = generateRandomHillfortId()
        hillforts.add(hillfort)
        serialize()
    }

    override fun update(hillfort: HillfortModel) {
        var foundHillfort: HillfortModel? = hillforts.find { p -> p.id == hillfort.id }
        if (foundHillfort != null) {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.addNotes = hillfort.addNotes
            foundHillfort.visited = hillfort.visited
            foundHillfort.favourited = hillfort.favourited
            foundHillfort.rating = hillfort.rating
            foundHillfort.dateVisited = hillfort.dateVisited
            foundHillfort.lat = hillfort.lat
            foundHillfort.lng = hillfort.lng
            foundHillfort.zoom = hillfort.zoom
            foundHillfort.address = hillfort.address
            foundHillfort.firstImage = hillfort.firstImage
            foundHillfort.secondImage = hillfort.secondImage
            foundHillfort.thirdImage = hillfort.thirdImage
            foundHillfort.fourthImage = hillfort.fourthImage
            serialize()
        }
    }

    override fun delete(placemark: HillfortModel) {
        hillforts.remove(placemark)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, HILLFORT_JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, HILLFORT_JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}