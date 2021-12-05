package crypt

import crypt.base.Scenario
import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef._

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._

import io.gatling.core.Predef.{atOnceUsers, constantUsersPerSec, heavisideUsers, nothingFor, rampUsers, rampUsersPerSec}
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef.http

import scala.collection.mutable.ListBuffer

class BasicSimulation extends Scenario {

  val restEndpoint = "http://localhost:8080/todo"
  var restEndpointData = """{ "title": "TODO",  "description": "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua." }"""

  // RUN test model
  /*
  setUp(runScenario(restEndpoint,restEndpointData).toList)
  /TODO AUTH
   */

  //

  val httpProtocol = http
      .baseUrl(keycloak_url) // Here is the root for all relative URLs
      .inferHtmlResources()
      .acceptHeader("*/*")
      .acceptEncodingHeader("gzip, deflate")
      .acceptLanguageHeader("fr,fr-fr;q=0.8,en-us;q=0.5,en;q=0.3")
      .connectionHeader("keep-alive")
      .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.10; rv:33.0) Gecko/20100101 Firefox/33.0")
      .silentResources // Silence all resources like css or css so they don't clutter the results

  // BUILD test model
  var numberOfUsers = 5
  var model: ListBuffer[PopulationBuilder] = ListBuffer();
  for (userNo <- 1 until numberOfUsers) {
    // TODO use loop
    // for ( userNo <- 1 to 5) {
    model += scenario("Tasks for for user " + userNo)
      .exec(http("Create")
          .post(s"${restEndpoint}/${userNo}")
          .headers(headers_http)
          .body(StringBody(restEndpointData)).asJson
          .check(status.is(200))
      )
      .exec(http("Read")
          .get(s"${restEndpoint}/${userNo}")
          .headers(headers_http)
          .check(status.is(200))
      )
      /*.pause(10)
      .exec(http("Read")
          .get(s"${restEndpoint}/${userNo}")
          .headers(headers_http)
          .check(status.is(200))
      )
      .exec(http("Create")
          .post(s"${restEndpoint}/${userNo+1}")
          .headers(headers_http)
          .body(StringBody(restEndpointData)).asJson
          .check(status.is(200))
      )
      .exec(http("Read")
          .post(s"${restEndpoint}/${userNo+1}")
          .headers(headers_http)
          .check(status.is(200))
      )
      .pause(10)
      .exec(http("Create")
          .post(s"${restEndpoint}/${userNo+2}")
          .headers(headers_http)
          .body(StringBody(restEndpointData)).asJson
          .check(status.is(200))
      )
      .exec(http("Read")
          .get(s"${restEndpoint}/${userNo+2}")
          .headers(headers_http)
          .check(status.is(200))
      )*/
    .inject(
      nothingFor(5.seconds), // Pause for a given duration.
      atOnceUsers(1), // Injects a given number of users at once.
      rampUsersPerSec(1).to(3).during(10.seconds).randomized, // Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
      rampUsers(2).during(5.seconds), // Injects a given number of users distributed evenly on a time window of a given duration.
    ).protocols(httpProtocol
        .inferHtmlResources()
        .disableFollowRedirect // We must follow redirects manually to get the xsrf token from the keycloak redirect
        .disableAutoReferer
    )
    // }
  }
  setUp(model.toList)

}
