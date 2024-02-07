package com.tearas.resizevideo.utils


import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson

class HandleSaveResult(private val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("result", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    fun save(pathInput: String, pathOutput: String) {
        editor.putString(pathOutput, pathInput)
        editor.commit()
     }

    fun delete(pathOutput: String) {
        editor.remove(pathOutput)
    }

    fun getPathInput(pathOutput: String): String {
         return sharedPreferences.getString(pathOutput, "")!!
    }
}
