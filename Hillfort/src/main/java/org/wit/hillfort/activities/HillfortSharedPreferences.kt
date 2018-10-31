package org.wit.hillfort.activities

import android.content.Context

class HillfortSharedPreferences(context: Context){

    val PREFERENCE_NAME = "CurrentUser"

    val PREF_VAL_USER_EMAIL = "CurrentUserEmail"
    val PREF_VAL_USER_PASSWORD = "CurrentUserPassword"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCurrentUserEmail() : String {
        return preference.getString(PREF_VAL_USER_EMAIL, "EmailNA")
    }

    fun setCurrentUserEmail(email : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_EMAIL, email)
        editor.apply()
    }

    fun getCurrentUserPassword() : String {
        return preference.getString(PREF_VAL_USER_PASSWORD, "PasswordNA")
    }

    fun setCurrentUserPassword(password : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_PASSWORD, password)
        editor.apply()
    }

}