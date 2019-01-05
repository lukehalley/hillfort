package org.wit.hillfort.models

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.Hillforts
import org.wit.hillfort.helpers.exists
import java.util.*

val HILLFORT_JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type


fun generateRandomHillfortId(): Long {
    return Random().nextLong()
}

class HillfortFirebaseStore : HillfortStore, AnkoLogger {

    var hillfortDatabase: DatabaseReference = FirebaseDatabase.getInstance().reference
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    val context: Context

    val hillforts = ArrayList<HillfortModel>()

    constructor (context: Context) {
        this.context = context
        if (exists(context, HILLFORT_JSON_FILE)) {
//            deserialize()
        }
    }

    override fun findAll(): MutableList<HillfortModel> {
        return hillforts
    }

    override fun create(hillfort: HillfortModel) {
        
        hillfort.id = generateRandomHillfortId()

        info { "KEY: " + auth.uid.toString() }

        var key = hillfortDatabase.child("users").child(auth.uid.toString()).child(Hillforts.FIREBASE_TASK).push().key

        hillfort.fbId = key!!

        hillforts.add(hillfort)

        hillfortDatabase.child("users").child(auth.uid.toString()).child(Hillforts.FIREBASE_TASK).child(key).setValue(hillfort)

//        serialize()
    }

    override fun clear() {
        hillforts.clear()
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
//            serialize()
        }
        hillfortDatabase.child("users").child(auth.uid.toString()).child(Hillforts.FIREBASE_TASK).child(hillfort.fbId).setValue(hillfort)
    }

    override fun delete(hillfort: HillfortModel) {
        hillfortDatabase.child("users").child(auth.uid.toString()).child(Hillforts.FIREBASE_TASK).child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
//        serialize()
    }

//    private fun serialize() {
//        val jsonString = gsonBuilder.toJson(hillforts, listType)
//        write(context, HILLFORT_JSON_FILE, jsonString)
//    }

//    private fun deserialize() {
//        val jsonString = read(context, HILLFORT_JSON_FILE)
//        hillforts = Gson().fromJson(jsonString, listType)
//    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        hillforts.clear()
//        hillfortDatabase.child("users").child(auth.uid.toString()).child("hillforts").addListenerForSingleValueEvent(valueEventListener)

        hillfortDatabase.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).child(Hillforts.FIREBASE_TASK).addListenerForSingleValueEvent(valueEventListener)

        info { "GOT THESE HILLFORTS: " + hillforts }

    }
}