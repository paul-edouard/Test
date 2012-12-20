package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDeletionPart {

	@Inject
	private IEventBroker broker;

	@Inject
	private ITodoModel model;
	private Combo combo;
	private ComboViewer comboViewer;

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		comboViewer = new ComboViewer(parent, SWT.NONE);

		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
		comboViewer.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				if (element instanceof Todo) {
					Todo todo = (Todo) element;
					return todo.getDescription();
				}
				return "";
			}

		});
		comboViewer.setInput(model.getTodos());
		// comboViewer.setSelection(new SelectionEvent(e))

		combo = comboViewer.getCombo();
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1,
				1));
		combo.select(0);

		Button btnNewButton = new Button(parent, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) comboViewer
						.getSelection();
				Object element = selection.getFirstElement();
				if (element instanceof Todo) {
					Todo todo = (Todo) element;
					model.deleteTodo(todo.getId());
					comboViewer.setInput(model.getTodos());
					combo.select(0);
					comboViewer.refresh();

					broker.send(MyEventConstants.TOPIC_TODO_DATA_UPDATE
							+ MyEventConstants.DELETE, todo);

				}
			}
		});
		btnNewButton.setText("Delete ToDo");

	}

	@Focus
	private void setFocus() {

	}

}
