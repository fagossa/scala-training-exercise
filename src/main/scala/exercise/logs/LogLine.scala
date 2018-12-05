package exercise.logs

import java.time.Instant

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
        // TODO: complete this
        Some(new LogLine(Instant.now(), Server(origin), Server(destination)))
      case _ =>
        None
    }
  }
}