package com.example.roomhandson.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomhandson.data.local.enitities.Note
import com.example.roomhandson.databinding.NoteLayoutBinding

class NotesAdapter(private val notes: List<Note>, private val onNoteClick: (Int) -> Unit) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(private val binding: NoteLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            with(binding) {
                with(notes[position]) {
                    tvTitle.text = title
                    tvNoteDesc.text = if (note.length > 164) note.slice(0..164).plus("...") else note
                    tvTouchTime.text = lastTouchTime

                    root.setOnClickListener {
                        onNoteClick(id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder = NoteViewHolder(
        NoteLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(position)
    }
}