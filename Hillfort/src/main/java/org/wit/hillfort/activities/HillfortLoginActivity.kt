package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
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
            showProgress()
            var users = app.users.findAll()
            var foundUser: UserModel? = users.find { p -> p.email == enteredEmail.text.toString() }

            if (foundUser != null) {
                auth.signInWithEmailAndPassword(foundUser.email, foundUser.password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                mypreference.setCurrentUserName(foundUser.name)
                                mypreference.setCurrentUserEmail(foundUser.email)
                                mypreference.setCurrentUserPassword(foundUser.password)
                                startActivityForResult(intentFor<HillfortListActivity>().putExtra("loggedInUser", enteredEmail.text.toString()), 0)
                            } else {
                                // If sign in fails, display a message to the user.
                                toast(R.string.toast_InvalidCreds)
                            }
                            if (!task.isSuccessful) {
                                toast(R.string.toast_AuthFail)
                            }
                            hideProgress()
                        }
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

//    fun doSignUp(email: String, password: String) {
//        showProgress()
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(view!!) { task ->
//            if (task.isSuccessful) {
//                view?.navigateTo(VIEW.LIST)
//            } else {
//                view?.toast("Sign Up Failed: ${task.exception?.message}")
//            }
//            view?.hideProgress()
//        }
//    }

}