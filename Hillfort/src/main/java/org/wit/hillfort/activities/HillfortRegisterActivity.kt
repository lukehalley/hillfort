package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
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
    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbarRegister.title = title
        setSupportActionBar(toolbarRegister)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        info("Hillfort Activity started..")

        app = application as MainApp

        registerButton.setOnClickListener {

            // OLD
            /////////////////////////////

//            var users = app.users.findAll()
//
//            if (enteredEmail.text.toString() !in users.toString()) {
//
//                user.name = enteredName.text.toString()
//                user.email = enteredEmail.text.toString()
//                user.password = enteredPassword.text.toString()
//                if (user.name.isEmpty() or user.email.isEmpty() or user.password.isEmpty()) {
//                    toast(R.string.hint_EnterHillfortTitle)
//                } else {
//                    if (enteredPassword.text.toString() == enteredPasswordConfirm.text.toString()) {
//                        app.users.create(user.copy())
//                        setResult(AppCompatActivity.RESULT_OK)
//                        toast(R.string.hint_SucessfullRegister)
//                        finish()
//                    } else {
//                        toast("Passwords Do Not Match!")
//                    }
//                }
//            } else {
//                toast(R.string.err_UserExists)
//            }

            // NEW
            /////////////////////////////

//            var users = app.users.findAll()
//
//            if (enteredEmail.text.toString() !in users.toString()) {
//
//                user.name = enteredName.text.toString()
//                user.email = enteredEmail.text.toString()
//                user.password = enteredPassword.text.toString()
//                if (user.name.isEmpty() or user.email.isEmpty() or user.password.isEmpty()) {
//                    toast(R.string.hint_EnterHillfortTitle)
//                } else {
//                    if (enteredPassword.text.toString() == enteredPasswordConfirm.text.toString()) {
//                        app.users.create(user.copy())
//                        setResult(AppCompatActivity.RESULT_OK)
//                        toast(R.string.hint_SucessfullRegister)
//                        finish()
//                    } else {
//                        toast("Passwords Do Not Match!")
//                    }
//                }
//            } else {
//                toast(R.string.err_UserExists)
//            }

            showProgress()

            // [START create_user_with_email]
            auth.createUserWithEmailAndPassword(enteredEmail.text.toString(), enteredPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (enteredName.text.toString().isEmpty() or enteredPassword.text.toString().isEmpty()) {
                            toast(R.string.hint_EnterAllFields)
                        } else {
                            if (enteredPassword.text.toString() == enteredPasswordConfirm.text.toString()) {
                                if (task.isSuccessful) {
                                    // Sign in success, update UI with the signed-in user's information
                                    toast(R.string.hint_SucessfullRegister)
                                    val user = auth.currentUser
                                    finish()
                                } else {
                                    toast("User Registration Failed!" + task.exception)
                                    info { "User Registration Failed!" + task.exception }
                                }
                            } else {
                                toast("Passwords Do Not Match!")
                            }
                        }

                        // [START_EXCLUDE]
                        hideProgress()
                        // [END_EXCLUDE]
                    }
            // [END create_user_with_email]

        }

    }

    fun showProgress() {
        loadingRegisterIndicator.visibility = View.VISIBLE
    }

    fun hideProgress() {
        loadingRegisterIndicator.visibility = View.GONE
    }

}

