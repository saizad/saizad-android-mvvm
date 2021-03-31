package com.saizad.mvvm.ui.calendar

import androidx.fragment.app.DialogFragment
import org.joda.time.DateTime

open class BaseCalendarPickerFragment(protected val dateSelectedListener: (DateTime) -> Unit
) : DialogFragment()