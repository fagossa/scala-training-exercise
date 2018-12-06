package exercise.logs

import exercise.logs.StatsFiltering.FilteredStream
import exercise.provider.LogProvider

object StatsCalculator {
  def apply(filters: FilteredStream[LogLine]*)(data: Stream[LogLine]): Stream[LogLine] =
    filters.foldLeft(data) {
      case (previousStream, newFilter) => newFilter.apply(previousStream)
    }
}

object StatsGenerator {
  def getLinesUsing(logProvider: LogProvider[String]): Stream[LogLine] =
    logProvider.lines()
      .map { line => LogLine.apply(line) }
      .collect { case Some(logLine) => logLine }
}

object StatsFiltering {

  type FilteredStream[A] = Stream[A] => Stream[A]

  def withDestination(server: Server): FilteredStream[LogLine] =
    data => data.filter { logLine => logLine.destination == server }

  def withOrigin(server: Server): FilteredStream[LogLine] =
    data => data.filter { logLine => logLine.origin == server }

  def within(range: Range): FilteredStream[LogLine] =
    data => data.filter { logLine => range.contains(logLine.timestamp) }

}

object StatsGrouping {

  type ReducedStream[A] = Stream[LogLine] => A

  case class RequestedServer(server: Server, amt: Int)

  def mostRequestedServer(range: Range): ReducedStream[Option[RequestedServer]] = {
    def count(stream: Stream[LogLine]): Int = stream.size
    def chooseBigger(previous: Option[RequestedServer], next: RequestedServer): Option[RequestedServer] = {
      previous match {
        case None => Some(next)
        case Some(RequestedServer(_, previousAmt)) if previousAmt < next.amt => Some(next)
        case _ => previous
      }
    }
    data =>
      StatsFiltering.within(range)(data)
        .groupBy { logLine => logLine.destination }
        .map { case (server, related) => (server, count(related)) }
        .foldLeft(None: Option[RequestedServer]) { case (previous, (server, amt)) =>
          chooseBigger(previous, RequestedServer(server, amt))
        }
  }


}