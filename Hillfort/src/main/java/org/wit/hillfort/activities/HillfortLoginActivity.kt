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

class HillfortLoginActivity : AppCompatActivity(), AnkoLogger {

    var auth: FirebaseAuth = FirebaseAuth.getInstance()

    lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?) {
        app = application as MainApp
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val mypreference = HillfortSharedPreferences(this)

        loginButton.setOnClickListener {

//            var users = app.users.findAll()
//            var foundUser: UserModel? = users.find { p -> p.email == enteredEmail.text.toString() }

            if (enteredEmail.text.toString().isNotEmpty() && enteredPassword.text.toString().isNotEmpty()) {
                showProgress()
                auth.signInWithEmailAndPassword(enteredEmail.text.toString(), enteredPassword.text.toString())
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                val user = auth.currentUser
                                mypreference.setCurrentUserName(enteredEmail.text.toString())
                                mypreference.setCurrentUserEmail(enteredEmail.text.toString())
                                mypreference.setCurrentUserPassword(enteredPassword.text.toString())
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
            } else if (enteredEmail.text.toString().isEmpty()){
                toast("Please Enter Your Email")
            } else if (enteredPassword.text.toString().isEmpty()){
                toast("Please Enter Your Email")
            } else {
                toast(R.string.hint_EnterAllFields)
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