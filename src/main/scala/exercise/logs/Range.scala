package exercise.logs

import java.time.Instant
import java.time.temporal.ChronoUnit

case class Range(begin: Instant, end: Instant) {
  def contains(instant: Instant): Boolean =
    begin.isBefore(instant) && end.isAfter(instant)
}

object Range {
  def oneHourFrom(begin: Instant): Range =
    Range(begin, begin.plus(1, ChronoUnit.HOURS))
}