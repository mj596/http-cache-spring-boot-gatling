package basic

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class NonBlockingDeferredResultNoCacheSimulation extends Simulation {

  val rampUpTimeSecs = 20 seconds
  val testTimeSecs   = 60
  val noOfUsers      = 200
  val minWaitMs      = 1000 milliseconds
  val maxWaitMs      = 3000 milliseconds

  val httpConf = http
    .baseURL("http://localhost:8080")
    .acceptHeader("application/json;q=0.9,*/*;q=0.8") // 6
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("NonBlockingDeferredResultNoCacheSimulation")
    .during(testTimeSecs) {
      exec(
        http("request_1")
          .get("/non-blocking-no-cache/json/deferredResult/businessUnit?businessUnit=Raben%20Logistics%20Polska&minResponseTime=500&maxResponseTime=2000")
          .check(status.is(200))
      )
        .pause(minWaitMs, maxWaitMs)
    }
  setUp(
    scn.inject(rampUsers(noOfUsers) over (rampUpTimeSecs))
  ).protocols(httpConf)
}