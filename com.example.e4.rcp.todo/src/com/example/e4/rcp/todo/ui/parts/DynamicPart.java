package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class DynamicPart {
	private Button btnCheckButton;

	public DynamicPart() {
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
						"Dynnamic are working");
			}
		});
		btnCheckButton.setText("Validate");

	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		btnCheckButton.setFocus();
	}

}
