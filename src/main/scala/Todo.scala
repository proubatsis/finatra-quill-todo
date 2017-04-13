/**
  * Created by panagiotis on 12/04/17.
  */

case class TodoList(id: Option[Int], title: String)
case class TodoItem(id: Option[Int], todoListId: Option[Int], title: String, completed: Boolean)
case class TodoListItems(id: Option[Int], title: String, items: Option[List[TodoItem]])

object TodoListItems {
  def combine(l: TodoList, i: List[TodoItem])  = {
    TodoListItems(l.id, l.title, Some(i))
  }
}