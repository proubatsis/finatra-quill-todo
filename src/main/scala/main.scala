import com.twitter.finagle.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.util.Await
import doobie.util.transactor.DriverManagerTransactor
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import doobie.imports._
import doobie.util.query

import scalaz.concurrent.Task

/**
  * Created by panagiotis on 11/04/17.
  */

case class TodoList(id: Int, title: String)

object Main extends App {
  val xa = DriverManagerTransactor[Task](
    "org.postgresql.Driver", "jdbc:postgresql:todosdb", "panagiotis", "mypass"
  )

  val a: ConnectionIO[List[String]] = sql"select title from todo_list".query[String].list
  a.transact(xa).unsafePerformSync.foreach(println)

  val hi: Endpoint[TodoList] = get("abc") {
    Ok(TodoList(1, "Panagiotis"))
  }

  val bye: Endpoint[TodoList] = get("xyz") {
    Ok(TodoList(2, "Roubatsis"))
  }

  val combined: Service[Request, Response] = (hi :+: bye).toService

  Await.ready(Http.server.serve(":8081", combined))
}
