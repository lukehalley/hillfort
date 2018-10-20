package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.wit.hillfort.R

class HillfortLoginActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            // Checking if the user enters the correct log in details
            if (enteredEmail.text.toString() == ("luke123halley@gmail.com") && enteredPassword.text.toString() == ("password")) {
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