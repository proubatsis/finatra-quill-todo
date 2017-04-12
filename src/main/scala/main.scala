import com.twitter.finagle.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, FuturePool}
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

object Main extends App {
  val getAll: Endpoint[List[TodoList]] = get("todos") {
    FuturePool.unboundedPool {
      Ok(TodoDB.findLists())
    }
  }

  val bye: Endpoint[TodoList] = get("xyz") {
    Ok(TodoList(2, "Roubatsis"))
  }

  val combined: Service[Request, Response] = (getAll :+: bye).toService

  Await.ready(Http.server.serve(":8081", combined))
}
