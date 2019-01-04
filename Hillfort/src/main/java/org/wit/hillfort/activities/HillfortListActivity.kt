package org.wit.hillfort.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp
        toolbarMain.title = title
        setSupportActionBar(toolbarMain)

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(app.hillforts.findAll(), this)



        loadHillforts()
        addHillfortFab.setOnClickListener() {
            startActivityForResult<HillfortActivity>(0)
        }

        var mDrawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            var user = UserModel()
            when (menuItem?.itemId) {
                R.id.drawerNav_addHillfort -> startActivityForResult<HillfortActivity>(0)
            }
            when (menuItem?.itemId) {
                R.id.drawerNav_AllHillforts -> startActivityForResult<HillfortAllMapsActivity>(0)
            }
            when (menuItem?.itemId) {
                R.id.drawerNav_FavHillforts -> startActivityForResult<HillfortFavListActivity>(0)
            }
            when (menuItem?.itemId) {
                R.id.drawerNav_Settings -> startActivityForResult(intentFor<HillfortSettingsActivity>().putExtra("user_edit", user), 0)
            }
            when (menuItem?.itemId) {
                R.id.drawerNav_Logout ->
                    alert(R.string.logoutPrompt) {
                        yesButton {
                            finish()
                        }
                        noButton {}
                    }.show()
            }
            mDrawerLayout.closeDrawers()
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var adapter = HillfortAdapter(app.hillforts.findAll(), this)
        menuInflater.inflate(R.menu.menu_main, menu)
        var searchItem : MenuItem? = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                toast("FIRE SUB")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                toast("FIRE CHANGE" + adapter)
                adapter?.filter?.filter(p0)
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        var mDrawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        var user = UserModel()
        when (item?.itemId) {
            android.R.id.home -> mDrawerLayout.openDrawer(GravityCompat.START)
        }
        when (item?.itemId) {
            R.id.item_add -> startActivityForResult<HillfortActivity>(0)
        }
        when (item?.itemId) {
            R.id.item_settings -> startActivityForResult(intentFor<HillfortSettingsActivity>().putExtra("user_edit", user), 0)
        }
        when (item?.itemId) {
            R.id.item_logout ->
                alert(R.string.logoutPrompt) {
                    yesButton {
                        finish()
                    }
                    noButton {}
                }.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel) {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        loadHillforts()
        toast("FIRE LOAD")
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillforts() {

        showHillforts(app.hillforts.findAll())
    }

    fun showHillforts(hillforts: List<HillfortModel>) {
        val mypreference = HillfortSharedPreferences(this)
        val userName = mypreference.getCurrentUserName()
        val userEmail = mypreference.getCurrentUserEmail()
        val parentView = nav_view.getHeaderView(0)
        val navHeaderUser = parentView.findViewById(R.id.current_user_nav_header) as TextView
        val navHeaderEmail = parentView.findViewById(R.id.current_email_nav_header) as TextView
        navHeaderUser.text = userName
        navHeaderEmail.text = userEmail
        mypreference.setCurrentHillfortCount(hillforts.size)
        var visCounted = 0
        hillforts.forEach {
            if (it.visited) {
                visCounted++
            }
        }
        mypreference.setCurrentVisitHillfortCount(visCounted)
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
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