/**
  * Created by panagiotis on 12/04/17.
  */

case class TodoList(id: Int, title: String)
case class TodoItem(id: Int, todoListId: Int, title: String, completed: Boolean)
