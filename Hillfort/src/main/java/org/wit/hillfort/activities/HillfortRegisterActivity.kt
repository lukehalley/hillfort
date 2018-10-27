package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class HillfortRegisterActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarRegister.title = title
        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        info("Hillfort Activity started..")

        app = application as MainApp

        registerButton.setOnClickListener {
            var users = app.users.findAll()
            if (enteredEmail.text.toString() !in users.toString()) {
                user.name = enteredName.text.toString()
                user.email = enteredEmail.text.toString()
                user.password = enteredPassword.text.toString()
                if (user.name.isEmpty() or user.email.isEmpty() or user.password.isEmpty()) {
                    toast(R.string.hint_EnterHillfortTitle)
                } else {
                    app.users.create(user.copy())
                    setResult(AppCompatActivity.RESULT_OK)
                    toast(R.string.hint_SucessfullRegister)
                    finish()
                }
            } else {
                toast(R.string.err_UserExists)
            }
        }

    }

}

