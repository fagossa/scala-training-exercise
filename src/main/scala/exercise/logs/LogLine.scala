package exercise.logs

import java.time.Instant
import scala.util.Try

case class Server(name: String) extends AnyVal {
  override def toString: String = name
}

class LogLine private(val timestamp: Instant, val origin: Server, val destination: Server) {
  override def toString: String = s"$timestamp $origin $destination"
}

object LogLine {
  def apply(line: String): Option[LogLine] = {
    line.split(" ").toList match {
      case timestamp :: origin :: destination :: Nil =>
        Try(timestamp.toLong)
          .map { value => Instant.ofEpochMilli(value) }
          .map { instant => new LogLine(instant, Server(origin), Server(destination)) }
          .toOption
      case _ =>
        None
    }
  }
}