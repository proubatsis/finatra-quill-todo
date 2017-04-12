import com.twitter.finagle.Http
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

  Await.ready(Http.server.serve(":8081", hi.toService))
}
