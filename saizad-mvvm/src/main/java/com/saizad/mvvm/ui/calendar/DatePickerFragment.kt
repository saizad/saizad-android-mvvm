package com.saizad.mvvm.ui.calendar

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.os.Bundle
import org.joda.time.DateTime

class DatePickerFragment(
    private val dateTime: DateTime = DateTime(),
    private val minDateTime: DateTime = DateTime(0),
    dateSelectedListener: (DateTime) -> Unit
) : BaseCalendarPickerFragment(dateSelectedListener) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { _, year, month, dayOfMonth ->
                dateSelectedListener.invoke(
                    DateTime.parse(year.toString() + "-" + (month + 1) + "-" + dayOfMonth)
                )
            },
            dateTime.year,
            dateTime.monthOfYear - 1,
            dateTime.dayOfMonth
        )
        datePickerDialog.datePicker.minDate = minDateTime.millis
        return datePickerDialog
    }

}