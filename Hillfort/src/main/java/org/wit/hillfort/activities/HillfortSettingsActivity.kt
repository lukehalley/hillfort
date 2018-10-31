package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel


class HillfortSettingsActivity : AppCompatActivity(), AnkoLogger {

    var user = UserModel()
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        toolbarSettings.title = "Settings"
        setSupportActionBar(toolbarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val mypreference = HillfortSharedPreferences(this)

        app = application as MainApp

        user = intent.extras.getParcelable<UserModel>("user_edit")
        info { "USER INFO " + user.name +  user.email + user.password}
        editUserEmail.setText(mypreference.getCurrentUserEmail())
        editUserPassword.setText(mypreference.getCurrentUserPassword())
        saveEditUser.setOnClickListener {
            user.name = user.name
            user.email = editUserEmail.text.toString()
            user.password = editUserPassword.text.toString()
            if (user.email.isEmpty() or user.password.isEmpty()) {
                toast(R.string.hint_EnterHillfortTitle)
            } else {
                alert(R.string.confirmUserEditSave) {
                    yesButton {
                        info { "UPDATING USE TO: " +  user}
                        app.users.update(user)
                        finish()
                    }
                    noButton {}
                }.show()
            }

        }

    }
}

