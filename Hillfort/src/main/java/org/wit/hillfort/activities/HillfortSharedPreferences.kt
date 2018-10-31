package org.wit.hillfort.activities

import android.content.Context

class HillfortSharedPreferences(context: Context){

    val PREFERENCE_NAME = "CurrentUser"

    val PREF_VAL_LOGIN_COUNT = "LoginCount"
    val PREF_VAL_USER_NAME = "CurrentUserName"
    val PREF_VAL_USER_EMAIL = "CurrentUserEmail"
    val PREF_VAL_USER_PASSWORD = "CurrentUserPassword"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getLoginCount() : Int {
        return preference.getInt(PREF_VAL_LOGIN_COUNT, 0)
    }

    fun setLoginCount(count : Int){
        val editor = preference.edit()
        editor.putInt(PREF_VAL_LOGIN_COUNT, count)
        editor.apply()
    }

    fun getCurrentUserName() : String {
        return preference.getString(PREF_VAL_USER_NAME, "NameNA")
    }

    fun setCurrentUserName(name : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_NAME, name)
        editor.apply()
    }

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