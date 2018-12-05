package exercise.provider

import scala.io.Source

trait LogProvider[A] {
  def lines(): Stream[A]
}

class FileLogProvider(fileName: String) extends LogProvider[String] {
  def lines(): Stream[String] =
    Source.fromFile(fileName, "UTF-8").getLines().toStream
}
