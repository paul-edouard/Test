package com.example.e4.rcp.todo.wizards;

import javax.inject.Inject;

import org.eclipse.jface.wizard.Wizard;

public class NewTodoWizard extends Wizard {

	@Inject
	TodoWizardPage1 page1;

	@Inject
	TodoWizardPage2 page2;

	public NewTodoWizard() {
		setWindowTitle("New Todo Wizard");

	}

	@Override
	public void addPages() {
		this.addPage(page1);
		this.addPage(page2);
	}

	@Override
	public boolean performFinish() {
		return page2.getBtnCheckButtonConfirm().getSelection();

	}

	@Override
	public boolean canFinish() {
		return page2.getBtnCheckButtonConfirm().getSelection();

	}

}
