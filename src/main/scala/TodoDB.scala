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

  def findList(id: Int): TodoListItems = {
    val tl: TodoList = sql"select id, title from todo_list where id=$id".query[TodoList].unique.transact(connection).unsafePerformSync
    val items: List[TodoItem] = sql"select id, todo_list_id, title, completed from todo_item where todo_list_id=$id".query[TodoItem].list.transact(connection).unsafePerformSync
    TodoListItems.combine(tl, items)
  }

  def insertList(list: TodoList): ConnectionIO[TodoList] = {
    val title = list.title
    sql"insert into todo_list (title) VALUES ($title)".update.withUniqueGeneratedKeys[TodoList]("id", "title")
  }

  def insertTodoItem(item: TodoItem, todoListId: Int): ConnectionIO[TodoItem] = {
    val tlid = todoListId
    val title = item.title
    val completed = item.completed
    sql"insert into todo_item (todo_list_id, title, completed) VALUES ($tlid, $title, $completed)".update.withUniqueGeneratedKeys[TodoItem]("id", "todo_list_id", "title", "completed")
  }

  def insertTodo(todo: TodoListItems): Option[TodoListItems] = {
    val l = insertList(TodoList(todo.id, todo.title)).transact(connection).unsafePerformSync
    val i = for {
      todoListId <- l.id
      items <- todo.items
    } yield items map (item => insertTodoItem(item, todoListId).transact(connection).unsafePerformSync)

    Some(TodoListItems(l.id, l.title, i))
  }

  def deleteTodoList(id: Int) = {
    val q: Update0 = sql"delete from todo_list where id=$id".update
    q.run.transact(connection).unsafePerformSync
  }

  def deleteTodoItems(todoListId: Int): Int = {
    val q: Update0 = sql"delete from todo_item where todo_list_id=$todoListId".update
    q.run.transact(connection).unsafePerformSync
  }

  def deleteTodo(id: Int) = {
    deleteTodoItems(id) + deleteTodoList(id)
  }
}
