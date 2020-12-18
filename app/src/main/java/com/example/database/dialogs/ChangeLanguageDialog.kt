package com.example.database.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.widget.Button
import android.widget.RadioGroup
import com.example.database.R
import com.example.database.database.LocalStorage

class ChangeLanguageDialog(context: Context, resources: Resources) : AlertDialog(context) {
    @SuppressLint("InflateParams")
    private val view = LayoutInflater.from(context).inflate(R.layout.change_language_dialog, null)
    private var oldCheck = LocalStorage.read("old_checked", R.id.rb1)
    var oldLanguage = "en"
    private var listener: ((String, Int) -> Unit)? = null
    fun confirm(f: (String, Int) -> Unit) {
        listener = f
    }

    fun checkRadio(id: Int) {
        view.findViewById<RadioGroup>(R.id.buttonRadioGroup).check(id)
    }

    init {
        setView(view)
        view.apply {
            findViewById<RadioGroup>(R.id.buttonRadioGroup).setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.rb1 -> oldLanguage = "en"
                    R.id.rb2 -> oldLanguage = "ru"
                }
                oldCheck = checkedId
            }
            findViewById<Button>(R.id.buttonConfirm).setOnClickListener {
                listener?.invoke(oldLanguage, oldCheck)
                cancel()
            }
        }
    }
}