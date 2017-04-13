import com.twitter.finagle.http._
import com.twitter.finagle.{Http, Service}
import com.twitter.util.{Await, FuturePool}
import doobie.util.transactor.DriverManagerTransactor
import io.finch._
import io.finch.circe._
import io.circe.generic.auto._
import doobie.imports._
import doobie.util.query

/**
  * Created by panagiotis on 11/04/17.
  */

object Main extends App {
  val getAll: Endpoint[List[TodoList]] = get("todos") {
    FuturePool.unboundedPool {
      Ok(TodoDB.findLists())
    }
  }

  val getById: Endpoint[TodoListItems] = get("todos" :: int) { id: Int =>
    Ok(TodoDB.findList(id))
  }

  val createTodo: Endpoint[TodoListItems] = post("todos" :: jsonBody[TodoListItems]) { todo: TodoListItems =>
    TodoDB.insertTodo(todo) match {
      case Some(t) => Created(t)
      case None => BadRequest(new Exception("invalid todo"))
    }
  }

  val deleteTodo: Endpoint[Int] = delete("todos" :: int) { id: Int =>
    Ok(TodoDB.deleteTodo(id))
  }

  val combined: Service[Request, Response] = (getAll :+: getById :+: createTodo :+: deleteTodo).toService

  Await.ready(Http.server.serve(":8081", combined))
}
