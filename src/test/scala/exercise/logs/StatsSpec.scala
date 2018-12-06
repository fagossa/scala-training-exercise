package exercise.logs

import java.time.Instant

import exercise.logs.StatsFiltering.FilteredStream
import exercise.logs.StatsGrouping.RequestedServer
import exercise.logs.StatsSpec.{defaultLines, filterUsing}
import org.scalatest._

class StatsSpec extends WordSpec with Inside with MustMatchers {

  "a StartsCalculator" must {

    "do nothing if no filters" in {
      val result = LogLine("1366815793 quark garak").get
      StatsCalculator()(List(result).toStream) mustBe List(result).toStream
    }

    "apply a filter" in {
      val lines = List(
        LogLine("1366815793 aaa bbb").get,
        LogLine("1366815793 bbb aaa").get
      )
      val filter: FilteredStream[LogLine] = data => data.filter(_ => false)
      StatsCalculator(filter)(lines.toStream) mustBe List.empty[LogLine].toStream
    }

    "filter by destination" in {
      filterUsing(defaultLines, StatsFiltering.withDestination(Server("bbb"))) { result =>
        result must have size 3
        result.map(_.destination).distinct.headOption mustBe Some(Server("bbb"))
      }
    }

    "filter by origin" in {
      filterUsing(defaultLines, StatsFiltering.withOrigin(Server("aaa"))) { result =>
        result must have size 3
        result.map(_.origin).distinct.headOption mustBe Some(Server("aaa"))
      }
    }

  }

  "a StatsGrouping" must {

    "identify most requested server in an hour" in {
      val ONE_HOUR = 1000 * 60 * 60
      val beginningOfTime = 1366815791L
      val lines = List(
        LogLine(s"${beginningOfTime + 1} aaa bbb").get,
        LogLine(s"${beginningOfTime + 2} aaa bbb").get,
        LogLine(s"${beginningOfTime + 3} bbb aaa").get,
        LogLine(s"${beginningOfTime + ONE_HOUR + 2} bbb aaa").get,
        LogLine(s"${beginningOfTime + ONE_HOUR + 3} bbb ccc").get
      )
      val result = StatsGrouping
        .mostRequestedServer(Range.oneHourFrom(Instant.ofEpochMilli(beginningOfTime)))
        .apply(lines.toStream)
      inside (result) {
        case Some(mostUsed) =>
          mostUsed mustBe RequestedServer(Server("bbb"), 2)
        case None => fail("server not found")
      }
    }

  }

}

object StatsSpec {

  val defaultLines: List[LogLine] = List(
    LogLine("1366815791 aaa bbb").get,
    LogLine("1366815792 aaa bbb").get,
    LogLine("1366815793 aaa bbb").get,
    LogLine("1366815794 bbb aaa").get,
    LogLine("1366815795 bbb ccc").get
  )

  def filterUsing(lines: List[LogLine], filterToApply: FilteredStream[LogLine])(callback: Stream[LogLine] => Unit): Unit = {
    val result = StatsCalculator(filterToApply)(lines.toStream)
    callback(result)
    ()
  }
}
