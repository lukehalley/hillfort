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
        val currEmail = mypreference.getCurrentUserEmail()
        val currPass = mypreference.getCurrentUserPassword()
        app = application as MainApp
        var users = app.users.findAll()
        user = intent.extras.getParcelable<UserModel>("user_edit")
        editUserEmail.setText(currEmail)
        editUserPassword.setText(currPass)
        var foundUser: UserModel? = users.find { p -> p.email == currEmail }
        saveEditUser.setOnClickListener {
            if (foundUser != null) {
                user.id = foundUser.id
                user.name = foundUser.name
            }
            user.email = editUserEmail.text.toString()
            user.password = editUserPassword.text.toString()
            if (user.email.isEmpty() or user.password.isEmpty()) {
                toast(R.string.hint_EnterHillfortTitle)
            } else {
                if (editUserPassword.text.toString() == editUserPasswordConfirm.text.toString()) {
                    alert(R.string.confirmUserEditSave) {
                        yesButton {
                            mypreference.setCurrentUserEmail(editUserEmail.text.toString())
                            mypreference.setCurrentUserPassword(editUserPassword.text.toString())
                            app.users.update(user)
                            finish()
                        }
                        noButton {}
                    }.show()
                } else {
                    toast("Passwords Do Not Match!")
                }
            }

        }

    }
}

