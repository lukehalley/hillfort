package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_fav_hillforts.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortFavListActivity : AppCompatActivity(), HillfortListener, AnkoLogger {

    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fav_hillforts)
        app = application as MainApp
        toolbarFavHillforts.title = title
        setSupportActionBar(toolbarFavHillforts)

        val layoutManager = LinearLayoutManager(this)
        hillfort_fav_list.layoutManager = layoutManager
        var favHills: List<HillfortModel> = app.hillforts.findAll().filter { s -> s.favourited }
        hillfort_fav_list.adapter = HillfortAdapter(favHills, this)
        loadFavHillforts()

    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadFavHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadFavHillforts() {
        showHillforts(app.hillforts.findAll().filter { s -> s.favourited })
    }

    fun showHillforts(hillforts: List<HillfortModel>) {
        hillfort_fav_list.adapter = HillfortAdapter(hillforts, this)
        hillfort_fav_list.adapter?.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        alert(R.string.logoutPrompt) {
            yesButton {
                finish()
                super.onBackPressed()
            }
            noButton {}
        }.show()
    }
}