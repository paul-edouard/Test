package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.model.Todo;
import com.example.e4.rcp.todo.ui.parts.TodoDetailsPart;

@Creatable
public class TodoWizardPage1 extends WizardPage {

	@Inject
	private Todo todo;

	/**
	 * Create the wizard.
	 */
	public TodoWizardPage1() {
		super("Page 1");
		setTitle("New Todo");
		setDescription("Enter the todo data");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		TodoDetailsPart part = new TodoDetailsPart();
		part.createControls(container);

		part.setTodo(todo);

		setControl(container);
	}

}
