package com.example.notessqlite

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.notessqlite.databinding.ActivityAddNoteBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var db: NoteDatabaseHelper
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = NoteDatabaseHelper(this)

        selectedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        selectedTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

        binding.dateEditText.setText(selectedDate)
        binding.timeEditText.setText(selectedTime)

        binding.selectDateButton.setOnClickListener {
            showDatePickerDialog()
        }

        binding.selectTimeButton.setOnClickListener {
            showTimePickerDialog()
        }


        binding.saveButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()
            val time = binding.timeEditText.text.toString()
            val date = binding.dateEditText.text.toString()

            if (!isValidTime(time)) {
                Toast.makeText(this, "Please enter a valid time (HH:mm)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!isValidDate(date)) {
                Toast.makeText(this, "Please enter a valid future date (yyyy-MM-dd)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (content.isEmpty()) {
                Toast.makeText(this, "Please enter content", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val note = Note(0, title, content, time, date)
            db.insertNote(note)
            finish()
            Toast.makeText(this, "Note Saved", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                selectedTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.timeEditText.setText(selectedTime)
            },
            hourOfDay,
            minute,
            true
        )
        timePickerDialog.show()    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, monthOfYear, dayOfMonth ->
                selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                binding.dateEditText.setText(selectedDate)
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()    }

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
