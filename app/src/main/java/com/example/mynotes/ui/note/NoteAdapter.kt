package com.example.mynotes.ui.note
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import com.example.mynotes.data.model.Note

class NoteAdapter(private val onNoteClicked: (Note) -> Unit) : ListAdapter<Note, NoteAdapter.NoteViewHolder>(
    NoteDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote = getItem(position)
        holder.bind(currentNote, onNoteClicked)
    }
    fun getNoteAt(position: Int): Note {
        return getItem(position)
    }
    fun removeItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
        submitList(newList)
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        private val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)

        fun bind(note: Note, onNoteClicked: (Note) -> Unit) {
            titleTextView.text = note.title
            contentTextView.text = note.content

            itemView.setOnClickListener {
                onNoteClicked(note)
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}
