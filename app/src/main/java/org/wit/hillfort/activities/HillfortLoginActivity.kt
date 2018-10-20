package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp

class HillfortLoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MainApp
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        loginButton.setOnClickListener {
            var users = app.users.findAll()
            if (enteredEmail.text.toString() in users.toString() && enteredPassword.text.toString() in users.toString()) {
                info { "Logged In!" }
                startActivityForResult<HillfortListActivity>(0)
            } else {
                toast(R.string.toast_InvalidCreds)
            }
        }

        navToRegisterButton.setOnClickListener {
            startActivityForResult<HillfortRegisterActivity>(0)
        }

    }


}