package exercise.logs

import java.time.Instant

import org.scalatest._

class LogLineSpec extends WordSpec with Inside with MustMatchers {

  "a LogLine" must {

    "parse valid lines" in {
      val result: Option[LogLine] = LogLine("1366815793 quark garak")
      inside (result) { case Some(logLine) =>
        logLine.timestamp mustBe Instant.ofEpochMilli(1366815793L)
        logLine.origin mustBe Server("quark")
        logLine.destination mustBe Server("garak")
      }
    }

    "reject lines with not enough parameters" in {
      val result: Option[LogLine] = LogLine("1366815793")
      result mustBe None
    }

    "reject lines with too much parameters" in {
      val result: Option[LogLine] = LogLine("1366815793 quark garak XXX")
      result mustBe None
    }

  }

}
