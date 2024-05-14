package com.example.notessqlite


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notessqlite.databinding.ActivityUpdateNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class UpdateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private var noteId: Int = -1

    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        selectedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.selectTimeButton.setOnClickListener {
            showTimePickerDialog()
        }


        noteId = intent.getIntExtra("note_id", -1)
        if (noteId== -1){
            finish()
            return
        }

        val note= db.getNoteByID(noteId)
        binding.updateTitleEditText.setText(note.title)
        binding.updateContentEditText.setText(note.content)
        binding.updateDateEditText.setText(note.date)
        binding.updateTimeEditText.setText(note.time)


        binding.updateSaveButton.setOnClickListener{
            val newTitle = binding.updateTitleEditText.text.toString()
            val newContent = binding.updateContentEditText.text.toString()
            val newTime = binding.updateTimeEditText.text.toString()
            val newDate = binding.updateDateEditText.text.toString()

            if (!isValidTime(newTime)) {
                Toast.makeText(this, "Please enter a valid time (HH:mm)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidDate(newDate)) {
                Toast.makeText(this, "Please enter a valid future date (yyyy-MM-dd)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newTitle.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newContent.isEmpty()) {
                Toast.makeText(this, "Please enter content", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            val updateNote = Note(noteId, newTitle, newContent, newTime, newDate)
            db.updateNote(updateNote)
            finish()
            Toast.makeText(this, "Changes Saved", Toast.LENGTH_SHORT).show()
        }

    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                binding.updateDateEditText.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()      }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.updateTimeEditText.setText(selectedTime)
            },
            hourOfDay,
            minute,
            true
        )
        timePickerDialog.show()      }

    private fun isValidTime(time: String): Boolean {
        val timeRegex = Regex("^([01]?[0-9]|2[0-3]):[0-5][0-9]\$")
        return time.matches(timeRegex)
    }

    private fun isValidDate(date: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Date()
        val inputDate = dateFormat.parse(date)

        return inputDate != null && inputDate >= currentDate
    }

}