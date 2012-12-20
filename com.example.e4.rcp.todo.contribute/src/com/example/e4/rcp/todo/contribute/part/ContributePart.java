package com.example.e4.rcp.todo.contribute.part;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class ContributePart {
	private Button btnCheckButton;

	public ContributePart() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent, final Shell shell) {

		btnCheckButton = new Button(parent, SWT.PUSH);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageDialog.openInformation(shell, "Dynamic",
						"Contribute are working");
			}
		});
		btnCheckButton.setText("Validate");

		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setBounds(10, 68, 55, 15);
		lblNewLabel.setText("Contribute");

	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		btnCheckButton.setFocus();
	}

}
