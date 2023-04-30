package at.sunilson.tahomaraffstorecontroller.mobile.shared.domain

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

object TimeUtils {

    /**
     * Use this to display LocalDate in the UI
     */
    val displayDateFormatter: DateTimeFormatter
        get() = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())

    /**
     * The clock used by [currentMillis]
     */
    var clock: java.time.Clock = java.time.Clock.systemUTC()

    /**
     * @return the current millisecond timestamp depending on the set [clock]
     *
     * By changing the [clock] this can for example always return a fixed timestamp in tests.
     */
    fun getCurrentMillis(): Long = Instant.now(clock).toEpochMilli()

    /**
     * @return [getCurrentMillis] converted to seconds
     */
    fun getCurrentSeconds(): Int = (getCurrentMillis() / 1000L).toInt()

    /**
     * Returns the current local date depending on the set [clock]
     */
    fun getCurrentLocalDate(): LocalDate = getLocalDateFromMillis(getCurrentMillis())

    fun getCurrentZoneDateTime(): ZonedDateTime = Instant.now(clock).atZone(ZoneId.systemDefault())

    /**
     * Returns a [LocalDate] for the given milliseconds [timestamp] in the system time zone
     */
    fun getLocalDateFromMillis(timestamp: Long): LocalDate = Instant
        .ofEpochMilli(timestamp)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
}
