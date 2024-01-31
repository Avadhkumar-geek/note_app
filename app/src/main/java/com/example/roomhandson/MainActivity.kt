package com.example.roomhandson

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.roomhandson.app.NotesApp
import com.example.roomhandson.databinding.ActivityMainBinding
import com.example.roomhandson.ui.home.NotesAdapter
import com.example.roomhandson.ui.note.AddNoteFragment
import com.example.roomhandson.utils.AppToast
import com.example.roomhandson.viewmodels.home.NotesViewModel
import com.example.roomhandson.viewmodels.home.NotesViewModelFactory


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private val notesViewModel: NotesViewModel by viewModels {
        NotesViewModelFactory((application as NotesApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noteFAB.setOnClickListener {
            onCreateNoteClickHandler(null)
        }

        setAdapter()
        fragmentResultHandler()
    }

    private fun setAdapter() {
        notesViewModel.allNote.observe(this) { note ->
            binding.rvNotes.adapter = NotesAdapter(note) {
                onCreateNoteClickHandler(it)
            }
        }
    }

    private fun onCreateNoteClickHandler(id: Int?) {
        binding.noteFAB.hide()
        supportFragmentManager.beginTransaction().replace(R.id.homeView, AddNoteFragment(id)).addToBackStack(null)
            .commit()
    }

    private fun fragmentResultHandler() {
        supportFragmentManager.setFragmentResultListener("createNote", this) { _, bundle ->
            val updatedNoteString = bundle.getString("updatedNoteBundle")
            val newNoteString = bundle.getString("newNoteBundle")
            val emptyNoteString = bundle.getString("emptyNoteBundle")
            val deleteNoteString = bundle.getString("deleteNoteBundle")

            val appToast = AppToast()

            if (emptyNoteString != null) appToast.showToast(emptyNoteString, this).show()

            if (updatedNoteString != null) appToast.showToast(updatedNoteString, this).show()

            if (newNoteString != null) appToast.showToast(newNoteString, this).show()

            if (deleteNoteString != null) {
                appToast.showSnackBar(deleteNoteString, binding.homeView).setAction("Undo") {
                    notesViewModel.restoreNote()
                }.show()

            }
        }
    }
}