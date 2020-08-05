package com.saizad.mvvm.utils

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.ChipGroup
import com.saizad.mvvm.components.SaizadBaseFragment
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.coroutines.*
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.format.PeriodFormatterBuilder
import kotlin.math.max


public val DateTime.dobDateFormat: String
    get() {
        return toString(Utils.DOB_DATE_FORMATTER)
    }

public val DateTime.appDateFormat: String
    get() {
        return toString(Utils.APP_DATE_FORMATTER)
    }


public val DateTime.appTimeFormat: String
    get() {
        return toString(Utils.APP_TIME_FORMATTER)
    }

public val DateTime.appTimeFormat24Hours: String
    get() {
        return toString(Utils.APP_TIME_FORMATTER_24_HOURS)
    }


public val DateTime.appTimeAndDateFormat: String
    get() {
        return "$appTimeFormat $appDateFormat"
    }

public val DateTime.ordinalDayOfMonth: String
    get() {
        return Utils.ordinal(dayOfMonth)
    }

public val DateTime.ordinalDay: String
    get() {
        return ordinalDayOfMonth + " " + toString("MMMM")
    }

public fun View.throttleClick(
    listener: () -> Unit
) {
    throttleClick(Consumer { listener.invoke() })
}

public fun View.throttleClick(
    consumer: Consumer<Any>,
    throwable: Consumer<Throwable> = Consumer {
        throw it
    },
    onComplete: Action = Action {}
) {
    ViewUtils.bindClick(this, consumer, throwable, onComplete)
}

fun <T> ChipGroup.getSelectedChipItems(): List<T> {
    return ViewUtils.getSelectedChipItems(this)
}

val DateTime.notificationTimeStamp: String
    get() {
        val period = Period(this, DateTime())
        val formatter = PeriodFormatterBuilder()
            .appendSeconds().appendSuffix(" sec", " secs").appendSuffix(" ago\n")
            .appendMinutes().appendSuffix(" min", " mins").appendSuffix(" ago\n")
            .appendHours().appendSuffix(" hour", " hours").appendSuffix(" ago\n")
            .appendDays().appendSuffix(" day", " days").appendSuffix(" ago\n")
            .appendWeeks().appendSuffix(" week", " weeks").appendSuffix(" ago\n")
            .appendMonths().appendSuffix(" month", " month").appendSuffix(" ago\n")
            .appendYears().appendSuffix(" year", " years").appendSuffix(" ago\n")
            .printZeroNever().rejectSignedValues(false)
            .toFormatter()
        val lines = formatter.print(period).lines()
        return lines[max(lines.size - 2, 0)]
    }

public fun SaizadBaseFragment<*>.lifecycleScopeOnMain(block: suspend CoroutineScope.() -> Unit): Job {
    return viewLifecycleOwner.lifecycleScopeOnMain(block)
}

public fun SaizadBaseFragment<*>.lifecycleScopeOnMain(timeMillis: Long, block: suspend CoroutineScope.() -> Unit): Job {
    return viewLifecycleOwner.lifecycleScopeOnMainWithDelay(timeMillis, block)
}

public fun LifecycleOwner.lifecycleScopeOnMain(block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.Main, block = block)
}

public fun LifecycleOwner.lifecycleScopeOnMainWithDelay(timeMillis: Long, block: suspend CoroutineScope.() -> Unit): Job {
    return lifecycleScope.launch(Dispatchers.Main){
        delay(timeMillis)
        block.invoke(this)
    }
}