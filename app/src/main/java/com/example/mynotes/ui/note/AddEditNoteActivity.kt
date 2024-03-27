package com.example.mynotes.ui.note

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.R

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText
    private var noteId: Int = -1 // Default to -1 to signify a new note

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)

        // Enable the Up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        editTextTitle = findViewById(R.id.edit_text_title)
        editTextContent = findViewById(R.id.edit_text_content)

        // Check if there's data passed to this activity for editing a note
        intent?.let {
            if (it.hasExtra(EXTRA_ID)) {
                noteId = it.getIntExtra(EXTRA_ID, -1)
                editTextTitle.setText(it.getStringExtra(EXTRA_TITLE))
                editTextContent.setText(it.getStringExtra(EXTRA_CONTENT))
            }
        }

        val saveButton: TextView = findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            saveNote()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Hide the keyboard and navigate up
        hideKeyboard()
        finish()
        return true
    }

    private fun saveNote() {
        val title = editTextTitle.text.toString().trim()
        val content = editTextContent.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare the result intent to pass back to MainActivity
        val data = Intent().apply {
            putExtra(EXTRA_TITLE, title)
            putExtra(EXTRA_CONTENT, content)
            // If editing, include the ID
            if (noteId != -1) {
                putExtra(EXTRA_ID, noteId)
            }
        }

        setResult(RESULT_OK, data)
        hideKeyboard()
        finish() // Close the activity
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(editTextTitle.windowToken, 0)
    }

    companion object {
        const val EXTRA_ID = "com.example.mynoteapp.EXTRA_ID"
        const val EXTRA_TITLE = "com.example.mynoteapp.EXTRA_TITLE"
        const val EXTRA_CONTENT = "com.example.mynoteapp.EXTRA_CONTENT"
    }
}
