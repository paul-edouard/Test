package com.example.e4.rcp.todo.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.wizards.NewTodoWizard;

public class NewTodoHandler {

	@Inject
	private ITodoModel todoModel;

	@Inject
	private IEventBroker broker;

	@Execute
	public void execute(Shell shell, IEclipseContext parent) {

		IEclipseContext newContext = parent.createChild();
		Todo todo = new Todo();
		newContext.set(Todo.class, todo);
		NewTodoWizard wizard = ContextInjectionFactory.make(
				NewTodoWizard.class, newContext);
		WizardDialog dialog = new WizardDialog(shell, wizard);
		int res = dialog.open();
		if (res == Window.OK) {
			todoModel.saveTodo(todo);
			broker.send(MyEventConstants.TOPIC_TODO_DATA_UPDATE
					+ MyEventConstants.NEW, todo);
		}

		/*
		 * System.out.println(getClass());
		 * 
		 * todo = new Todo(0, "", "", false, null);
		 * 
		 * TodoWizard wizard = new TodoWizard(todo);
		 * 
		 * WizardDialog dialog = new WizardDialog(shell, wizard); int res =
		 * dialog.open(); if (res == Window.OK) { todoModel.saveTodo(todo); }
		 */
	}

	@CanExecute
	public boolean canExecute() {
		return true;
	}

}