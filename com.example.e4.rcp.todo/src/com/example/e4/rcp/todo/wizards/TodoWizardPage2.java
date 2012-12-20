package com.example.e4.rcp.todo.wizards;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

@Creatable
public class TodoWizardPage2 extends WizardPage {
	private Button btnCheckButtonConfirm;

	/**
	 * Create the wizard.
	 */
	public TodoWizardPage2() {
		super("Page 2");
		setTitle("Page 2");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);

		btnCheckButtonConfirm = new Button(container, SWT.CHECK);
		btnCheckButtonConfirm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				TodoWizardPage2.this.getWizard().getContainer().updateButtons();
			}
		});
		btnCheckButtonConfirm.setBounds(0, 0, 93, 16);
		btnCheckButtonConfirm.setText("Confirm");
	}

	public Button getBtnCheckButtonConfirm() {
		return btnCheckButtonConfirm;
	}

}
