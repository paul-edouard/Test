package com.example.e4.rcp.todo.service;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.service.internal.MyTodoModelImpl;

public class TodoService {

	private static ITodoModel todoService = new MyTodoModelImpl();

	public static ITodoModel getInstance() {
		return todoService;
	}

}
