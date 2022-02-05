package com.example.simpletodo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditItemActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.edit_item)

            val data = intent
            val existingTask = data.getStringExtra("item").toString()
            val taskPosition = data.getIntExtra("pos", 0)
            val saveBtn = findViewById<Button>(R.id.saveBtn)
            val etTask = findViewById<EditText>(R.id.editTextTextPersonName)
            etTask.setText(existingTask)

            //clicked save button and returns the edited text to main activity
            saveBtn.setOnClickListener {
                data.putExtra("text", etTask.text.toString())
                data.putExtra("pos", taskPosition)
                setResult(RESULT_OK, data)
                finish()
            }
        }
}