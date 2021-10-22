package crypt.base

import io.gatling.core.Predef._
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef._

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._


abstract class Scenario extends Simulation {

  val keycloak_url = "http://localhost:9090"
  val realm = "app"

  val headers_http = Map(
    "Accept" -> """application/json"""
  )

  val headers_kc = Map(
    "Accept" -> """application/json""",
    // "X-XSRF-TOKEN" -> "${xsrf_token}"
  )
  val headers_http_authenticated = Map(
    "Accept" -> """application/json""",
    "Content-Type" -> "multipart/form-data",
    "Authorization" -> "Bearer ${access_token}"
  )
  val keycloakHeaders = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
    "Upgrade-Insecure-Requests" -> "1"
  )

  def authUri(url: String, realm: String): String = url + "/auth/realms/" + realm + "/protocol/openid-connect/auth"
  def tokenUri(url: String, realm: String): String = url + "/auth/realms/" + realm + "/protocol/openid-connect/token"
  def accountUri(url: String, realm: String): String = url + "/auth/realms/" + realm + "/account/"

  def keycloak(user: String, password: String, realm: String, restEndpoint: String, restEndpointData: Map[String, String]) = scenario("OAuth Login for " + realm + " and user " + user)
      .exec(http("First unauthenticated request")
          .get(accountUri(keycloak_url,realm))
          .headers(headers_http)
          .check(status.is(401))
        //.check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))
      ).exitHereIfFailed
      .pause(10)
      .exec(http("Authentication")
          .get(accountUri(keycloak_url,realm))
          .check(
              status.in(200, 302), //both statuses are valid for this request
              status.transform( status => 302.equals(status) ).saveAs("UNAUTH") // in case of redirect, make auth request
            )
        ).doIf("${UNAUTH}") {
              exec(
                session =>  session.set("loginUrl", session.attributes.filterKeys(key => key.startsWith("Location")))
              )
              .exec(http("Login Redirect")
                  .get("${loginUrl}")
                  //.silent
                  .check(css("#kc-form-login", "action").saveAs("kc-form-login"))).exitHereIfFailed
              .pause(10)
              .exec(http("Authenticate")
                  .post("${kc-form-login}")
                  //.silent
                  .headers(keycloakHeaders)
                  .formParam("username", user)
                  .formParam("password", password)
                  .formParam("submit", "Login")
                  .check(status.is(302))
                  .check(header("Location").saveAs("afterLoginUrl"))).exitHereIfFailed
              .pause(2)
              .exec(http("After Login Redirect")
                  .get("${afterLoginUrl}")
                  //.silent
                  .check(status.is(302))
                  .check(header("Location").saveAs("finalRedirectUrl"))
                //  .check(headerRegex("Set-Cookie", "XSRF-TOKEN=(.*);[\\s]").saveAs("xsrf_token"))
              )
              .exec(http("Open Account Page")
                  .get(accountUri(keycloak_url,realm))
                  .check(status.is(200))).exitHereIfFailed
      
        .pause(2)
        .exec(http("Create entity")
            .post(restEndpoint)
            .headers(headers_http_authenticated)
            .formParamMap(restEndpointData)
            .check(status.is(200))
        )

  }

  def runScenario(restEndpoint: String, restEndpointData: Map[String, String]): ListBuffer[PopulationBuilder] = {

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
      model += keycloak("user" + userNo, "user" + userNo, "app", restEndpoint, restEndpointData).inject(
        nothingFor(5.seconds), // Pause for a given duration.
        atOnceUsers(2), // Injects a given number of users at once.
        rampUsers(2).during(5.seconds), // Injects a given number of users distributed evenly on a time window of a given duration.
        constantUsersPerSec(2).during(30.seconds).randomized, // Injects users at a constant rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
        rampUsersPerSec(1).to(2).during(1.minutes).randomized, // Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at randomized intervals.
        heavisideUsers(1).during(20.seconds) // Injects a given number of users following a smooth approximation of the heaviside step function stretched to a given duration (seconds)
      ).protocols(httpProtocol
          .inferHtmlResources()
          .disableFollowRedirect // We must follow redirects manually to get the xsrf token from the keycloak redirect
          .disableAutoReferer
      )
      // }
    }
    return model;
  }
}
