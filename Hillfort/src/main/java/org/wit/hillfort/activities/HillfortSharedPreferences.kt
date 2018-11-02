package org.wit.hillfort.activities

import android.content.Context

class HillfortSharedPreferences(context: Context){

    val PREFERENCE_NAME = "CurrentUser"
    val PREF_VAL_USER_NAME = "CurrentUserName"
    val PREF_VAL_USER_EMAIL = "CurrentUserEmail"
    val PREF_VAL_USER_PASSWORD = "CurrentUserPassword"
    val PREF_VAL_HILLFORT_COUNT= "CurrentHillfortCount"
    val PREF_VAL_HILLFORT_VISITED_COUNT= "CurrentVisitedHillfortCount"

    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getCurrentUserName() : String {
        return preference.getString(PREF_VAL_USER_NAME, "Name NA")
    }

    fun setCurrentUserName(name : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_NAME, name)
        editor.apply()
    }

    fun getCurrentUserEmail() : String {
        return preference.getString(PREF_VAL_USER_EMAIL, "Email NA")
    }

    fun setCurrentUserEmail(email : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_EMAIL, email)
        editor.apply()
    }

    fun getCurrentUserPassword() : String {
        return preference.getString(PREF_VAL_USER_PASSWORD, "Password NA")
    }

    fun setCurrentUserPassword(password : String){
        val editor = preference.edit()
        editor.putString(PREF_VAL_USER_PASSWORD, password)
        editor.apply()
    }

    fun getCurrentHillfortCount() : Int {
        return preference.getInt(PREF_VAL_HILLFORT_COUNT, 0)
    }

    fun setCurrentHillfortCount(count : Int){
        val editor = preference.edit()
        editor.putInt(PREF_VAL_HILLFORT_COUNT, count)
        editor.apply()
    }

    fun getCurrentVisitHillfortCount() : Int {
        return preference.getInt(PREF_VAL_HILLFORT_VISITED_COUNT, 0)
    }

    fun setCurrentVisitHillfortCount(count : Int){
        val editor = preference.edit()
        editor.putInt(PREF_VAL_HILLFORT_VISITED_COUNT, count)
        editor.apply()
    }

}