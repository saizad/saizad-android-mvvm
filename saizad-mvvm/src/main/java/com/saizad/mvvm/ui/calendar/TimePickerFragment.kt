package com.saizad.mvvm.ui.calendar

import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.widget.TimePicker
import org.joda.time.DateTime

class TimePickerFragment(
    private val dateTime: DateTime,
    dateSelectedListener: (DateTime) -> Unit
) : BaseCalendarPickerFragment(dateSelectedListener), OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return TimePickerDialog(
            activity, this, dateTime.hourOfDay, dateTime.minuteOfHour().get(),
            false
        )
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        val dateTime =
            dateTime.withTimeAtStartOfDay().plusHours(hourOfDay).plusMinutes(minute)
        dateSelectedListener.invoke(dateTime)
    }

}