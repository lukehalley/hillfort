package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.info
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

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

    }


}