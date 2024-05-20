package com.example.fruit_selection_app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import androidx.appcompat.app.AppCompatActivity

class FruitSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fruit_selection)

        val appleCheckBox: CheckBox = findViewById(R.id.checkbox_apple)
        val bananaCheckBox: CheckBox = findViewById(R.id.checkbox_banana)
        val cherryCheckBox: CheckBox = findViewById(R.id.checkbox_cherry)
        val submitButton: Button = findViewById(R.id.button_submit)

        submitButton.setOnClickListener {
            val selectedFruits = mutableListOf<String>()
            if (appleCheckBox.isChecked) selectedFruits.add("Apple")
            if (bananaCheckBox.isChecked) selectedFruits.add("Banana")
            if (cherryCheckBox.isChecked) selectedFruits.add("Cherry")

            val intent = Intent()
            intent.putStringArrayListExtra("selectedFruits", ArrayList(selectedFruits))
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}

