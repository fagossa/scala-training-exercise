package exercise

import exercise.logs._
import exercise.provider.FileLogProvider

object Main extends App {
  val provider = new FileLogProvider("data.log")

  val toGarak: Stream[LogLine] =
    StatsCalculator(
      StatsFiltering.withDestination(Server("garak"))
    )(StatsGenerator.getLinesUsing(provider))

  toGarak.foreach(println)
}
