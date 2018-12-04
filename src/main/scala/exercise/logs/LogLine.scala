package exercise.logs

import java.time.Instant
import scala.util.Try

// TODO: create a class Server
case class Server(name: String) extends AnyVal {
  override def toString: String = name
}

/*
TODO: create a class LogLine having: timestamp, origin, destination
 */

object LogLine {
  /*
   * TODO: create a builder method 'apply' that transforms a String into an Option[LogLine
   * Use 'Instant.ofEpochMilli(value)' to create an Instant
   */
}