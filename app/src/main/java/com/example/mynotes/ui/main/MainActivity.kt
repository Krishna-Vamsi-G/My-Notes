package com.example.mynotes.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.ui.note.AddEditNoteActivity
import com.example.mynotes.ui.note.AddEditNoteActivity.Companion.EXTRA_CONTENT
import com.example.mynotes.ui.note.AddEditNoteActivity.Companion.EXTRA_ID
import com.example.mynotes.ui.note.AddEditNoteActivity.Companion.EXTRA_TITLE
import com.example.mynotes.data.model.Note
import com.example.mynotes.ui.note.NoteAdapter
import com.example.mynotes.ui.note.NoteViewModel
import com.example.mynotes.R
import com.example.mynotes.utils.SwipeToDeleteCallback
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NoteAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize RecyclerView and its adapter
        recyclerView = findViewById(R.id.notes_recyclerview)
        adapter = NoteAdapter { note ->
            // Handle the note click event
            val intent = Intent(this, AddEditNoteActivity::class.java).apply {
                putExtra(EXTRA_ID, note.id)
                putExtra(EXTRA_TITLE, note.title)
                putExtra(EXTRA_CONTENT, note.content)
            }
            startActivityForResult(intent, EDIT_NOTE_ACTIVITY_REQUEST_CODE)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize the ViewModel
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        // Observe the LiveData of notes from the ViewModel
        noteViewModel.allNotes.observe(this, Observer { notes ->
            // Update the cached copy of the notes in the adapter.
            notes?.let { adapter.submitList(it) }
        })

        // Floating Action Button for adding a new note
        val fab: FloatingActionButton = findViewById(R.id.fab_add_note)
        fab.setOnClickListener {
            // Launch AddEditNoteActivity for result
            startActivityForResult(
                Intent(this, AddEditNoteActivity::class.java),
                NEW_NOTE_ACTIVITY_REQUEST_CODE
            )
        }

        // Set up the RecyclerView item touch helper for swipe to delete
        val swipeHandler = object : SwipeToDeleteCallback(this) {
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                val position = viewHolder.adapterPosition
//                val noteToDelete = adapter.getNoteAt(position)
//                noteViewModel.delete(noteToDelete)
//            }

            override fun onDeleteConfirmed(viewHolder: RecyclerView.ViewHolder) {
                val position = viewHolder.adapterPosition
                adapter.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    // Handle the result from AddEditNoteActivity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            val title = data?.getStringExtra(EXTRA_TITLE) ?: ""
            val content = data?.getStringExtra(EXTRA_CONTENT) ?: ""
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

            if (requestCode == NEW_NOTE_ACTIVITY_REQUEST_CODE) {
                if (title.isNotEmpty() || content.isNotEmpty()) {
                    val newNote = Note(title = title, content = content, timestamp = timestamp)
                    noteViewModel.insert(newNote)
                    Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Empty note not saved", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == EDIT_NOTE_ACTIVITY_REQUEST_CODE) {
                val id = data?.getIntExtra(EXTRA_ID, -1) ?: -1
                if (id != -1 && (title.isNotEmpty() || content.isNotEmpty())) {
                    val updatedNote = Note(id = id, title = title, content = content, timestamp = timestamp)
                    noteViewModel.update(updatedNote)
                    Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Empty note not updated", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(this, "Note not saved", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        const val NEW_NOTE_ACTIVITY_REQUEST_CODE = 1
        const val EDIT_NOTE_ACTIVITY_REQUEST_CODE = 2
    }
}
