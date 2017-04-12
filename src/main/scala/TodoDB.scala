import com.twitter.util.{Future, FuturePool}
import doobie.imports._

import scalaz.concurrent.Task

/**
  * Created by panagiotis on 12/04/17.
  */
object TodoDB {
  val connection = DriverManagerTransactor[Task](
    "org.postgresql.Driver", "jdbc:postgresql:todosdb", "panagiotis", "mypass"
  )

  def findLists(): List[TodoList] = {
      val q: ConnectionIO[List[TodoList]] = sql"select id, title from todo_list".query[TodoList].list
      q.transact(connection).unsafePerformSync
  }
}
