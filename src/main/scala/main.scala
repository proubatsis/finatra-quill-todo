import com.twitter.finagle.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._

/**
  * Created by panagiotis on 11/04/17.
  */

object Main extends App {
  case class Person(name: String, age: Int)

  val hi: Endpoint[Person] = get("abc") {
    Ok(Person("Panagiotis", 20))
  }

  val bye: Endpoint[Person] = get("xyz") {
    Ok(Person("Roubatsis", 1000))
  }

  val combined: Service[Request, Response] = (hi :+: bye).toService

  Await.ready(Http.server.serve(":8081", combined))
}
