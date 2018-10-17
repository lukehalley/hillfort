package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class HillfortLoginActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginButton.setOnClickListener {
            // Checking if the user enters the correct log in details and assigning it to a variable to use
            var loggedIn =
                    if (enteredEmail.text.toString().equals("luke123halley@gmail.com") && enteredPassword.text.toString().equals("password")) {
                        info { "Logged In!" }
                    } else {
                        toast(R.string.toast_InvalidCreds)
                    }
        }

    }


}