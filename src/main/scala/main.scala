import com.twitter.finagle.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import io.getquill._

/**
  * Created by panagiotis on 11/04/17.
  */

case class Person(id: Int, name: String, age: Int)
case class Pet(ownerId: Int, name: String)

object Main extends App {
  val ctx = new SqlMirrorContext[MirrorSqlDialect, SnakeCase]
  import ctx._

  val q = quote {
    for {
      person <- query[Person].filter(_.name == "Panagiotis")
      pet <- query[Pet].filter(_.ownerId == person.id)
    } yield (person.name, pet.name)
  }

  println(ctx.run(q))

  val hi: Endpoint[Person] = get("abc") {
    Ok(Person(1, "Panagiotis", 20))
  }

  val bye: Endpoint[Person] = get("xyz") {
    Ok(Person(2, "Roubatsis", 1000))
  }

  val combined: Service[Request, Response] = (hi :+: bye).toService

  Await.ready(Http.server.serve(":8081", combined))
}
