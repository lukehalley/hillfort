package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class HillfortLoginActivity : AppCompatActivity(), AnkoLogger {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MainApp
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mypreference = HillfortSharedPreferences(this)
        loginButton.setOnClickListener {
            var users = app.users.findAll()
            var foundUser: UserModel? = users.find { p -> p.email == enteredEmail.text.toString() }
            if (foundUser != null) {
                if (enteredEmail.text.toString() == foundUser.email && enteredPassword.text.toString() == foundUser.password) {
                    mypreference.setCurrentUserName(foundUser.name)
                    mypreference.setCurrentUserEmail(foundUser.email)
                    mypreference.setCurrentUserPassword(foundUser.password)
                    startActivityForResult(intentFor<HillfortListActivity>().putExtra("loggedInUser", enteredEmail.text.toString()), 0)
                } else {
                    toast(R.string.toast_InvalidCreds)
                }
            } else {
                error { "foundUser is null" }
            }
        }
        navToRegisterButton.setOnClickListener {
            startActivityForResult<HillfortRegisterActivity>(0)
        }
    }

    fun showProgress() {
        loadingLoginIndicator.visibility = View.VISIBLE
    }

    fun hideProgress() {
        loadingLoginIndicator.visibility = View.GONE
    }

}