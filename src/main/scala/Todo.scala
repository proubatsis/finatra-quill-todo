/**
  * Created by panagiotis on 12/04/17.
  */

case class TodoList(id: Int, title: String)
case class TodoItem(id: Int, todoListId: Int, title: String, completed: Boolean)
case class TodoListItems(id: Int, title: String, items: List[TodoItem])

object TodoListItems {
  def combine(l: TodoList, i: List[TodoItem])  = {
    TodoListItems(l.id, l.title, i)
  }
}