package com.sporyap.sporyap.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

class KeyBoardEvents {
    companion object{

        fun hideKeyBoard(activity: Activity){
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            val view = activity.currentFocus
            if (view != null) {
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        fun showKeyBoard(editText: EditText, activity: Activity){
            val inputMethodManager = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

            val view = activity.currentFocus
            if (view != null) {
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
            }
        }

        fun showSoftKeyboard(view: View?, activity: Activity) {
            if (view!!.requestFocus()) {
                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }
        }
    }
}