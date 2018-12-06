package exercise.logs

import exercise.logs.StatsFiltering.FilteredStream
import org.scalatest._

class StatsCalculatorSpec extends WordSpec with Inside with MustMatchers {

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
      StatsCalculatorSpec.filterUsing(StatsFiltering.withDestination(Server("bbb"))) { result =>
        result must have size 3
        result.map(_.destination).distinct.headOption mustBe Some(Server("bbb"))
      }
    }

    "filter by origin" in {
      StatsCalculatorSpec.filterUsing(StatsFiltering.withOrigin(Server("aaa"))) { result =>
        result must have size 3
        result.map(_.origin).distinct.headOption mustBe Some(Server("aaa"))
      }
    }

  }

}

object StatsCalculatorSpec {
  def filterUsing(filterToApply: FilteredStream[LogLine])(callback: Stream[LogLine] => Unit): Unit = {
    val lines = List(
      LogLine("1366815791 aaa bbb").get,
      LogLine("1366815792 aaa bbb").get,
      LogLine("1366815793 aaa bbb").get,
      LogLine("1366815794 bbb aaa").get,
      LogLine("1366815795 bbb ccc").get
    )
    val result = StatsCalculator(filterToApply)(lines.toStream)
    callback(result)
    ()
  }
}
