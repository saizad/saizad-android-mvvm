package com.saizad.mvvm.ui.calendar

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import org.joda.time.DateTime

class DateTimePickerFragment(
    private val dateTime: DateTime = DateTime(),
    private val minDateTime: DateTime = DateTime(0),
    dateSelectedListener: (DateTime) -> Unit
) : BaseCalendarPickerFragment(dateSelectedListener) {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datePickerDialog = DatePickerDialog(
            requireContext(),
            OnDateSetListener { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    activity,
                    OnTimeSetListener { view1: TimePicker?, hourOfDay: Int, minute: Int ->
                        val dateTime =
                            DateTime.parse(year.toString() + "-" + (month + 1) + "-" + dayOfMonth)
                        dateSelectedListener.invoke(
                            dateTime.withTimeAtStartOfDay().plusHours(hourOfDay).plusMinutes(minute)
                        )
                    },
                    dateTime.hourOfDay,
                    dateTime.minuteOfHour().get(),
                    false
                ).show()
            },
            dateTime.year,
            dateTime.monthOfYear - 1,
            dateTime.dayOfMonth
        )
        datePickerDialog.datePicker.minDate = minDateTime.millis
        return datePickerDialog
    }

}