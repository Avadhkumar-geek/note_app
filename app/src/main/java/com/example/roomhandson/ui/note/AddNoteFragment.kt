package com.example.roomhandson.ui.note

import android.os.Build
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.roomhandson.MainActivity
import com.example.roomhandson.R
import com.example.roomhandson.data.local.enitities.Note
import com.example.roomhandson.databinding.AddNoteLayoutBinding
import com.example.roomhandson.viewmodels.home.NotesViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.time.LocalTime


class AddNoteFragment(var id: Int?) : Fragment() {

    private lateinit var binding: AddNoteLayoutBinding
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var activity: FragmentActivity
    private lateinit var time: LocalTime
    private lateinit var lastTouchTime: String
    private lateinit var note: Note

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity = requireActivity()

        time = LocalTime.now()
        lastTouchTime = "${time.hour}:${time.minute}"
        notesViewModel = ViewModelProvider(activity)[NotesViewModel::class.java]

        if (id != null) {
            binding.saveButton.text = getString(R.string.update)
            lifecycleScope.launch {

                note = notesViewModel.getNoteById(id!!)
                with(binding) {
                    tvTitle.setText(note.title)
                    tvNote.setText(note.note)
                    tvLastEdited.text = getString(R.string.editedOn).plus(" ${note.lastTouchTime}")
                }
            }
        } else {
            binding.tvLastEdited.text = getString(R.string.editedOn).plus(" $lastTouchTime")

        }

        binding.saveButton.setOnClickListener {
            handleOnSave()
        }

        binding.toolbar.setNavigationOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {

                R.id.clearAll -> {
                    clearNoteData()
                }

                R.id.delete -> {
                    id?.let {
                        deleteNote()
                    }
                }
            }
            true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleOnSave() {
        val title = binding.tvTitle.text.toString()
        val noteTxt = binding.tvNote.text.toString()

        val isIdNull = id == null

        if (title.isEmpty() && noteTxt.isEmpty()) {
            if (!isIdNull) {
                notesViewModel.deleteNote(note)
            }
            mFragmentResult("emptyNoteBundle", "Empty note discarded")

        } else {
            note = Note(0, title, noteTxt, lastTouchTime)

            if (!isIdNull) {
                note.id = id!!
                updateNote(note)
                mFragmentResult("updatedNoteBundle", "Note updated!")
            } else {
                createNote(note)
                mFragmentResult("newNoteBundle", "Note created!")
            }
        }

        activity.supportFragmentManager.popBackStack()
    }

    private fun createNote(note: Note) {
        notesViewModel.createNote(note)
    }

    private fun updateNote(note: Note) {
        notesViewModel.updateNote(note)
    }

    private fun deleteNote() {
        MaterialAlertDialogBuilder(requireActivity()).setTitle("Delete this note?")
            .setMessage("This action can't be undone").setPositiveButton("Delete") { _, _ ->
                notesViewModel.deleteNote(note)
                mFragmentResult("deleteNoteBundle", "Note moved to trash")
                activity.supportFragmentManager.popBackStack()
            }.setNegativeButton("Cancel") { _, _ -> }.create().show()
    }

    private fun mFragmentResult(bundleKey: String, message: String) {
        setFragmentResult("createNote", bundleOf(bundleKey to message))
    }

    private fun clearNoteData() {
        with(binding) {
            tvTitle.text?.clear()
            tvNote.text?.clear()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = AddNoteLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val inflater = TransitionInflater.from(requireContext())
        enterTransition = inflater.inflateTransition(android.R.transition.slide_right)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).binding.noteFAB.show()
    }
}