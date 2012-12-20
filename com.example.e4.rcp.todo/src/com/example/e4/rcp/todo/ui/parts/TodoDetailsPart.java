package com.example.e4.rcp.todo.ui.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.model.ITodoModel;
import com.example.e4.rcp.todo.model.Todo;

public class TodoDetailsPart implements Listener {
	private DataBindingContext m_bindingContext = new DataBindingContext();
	private Text summary;
	private Todo todo;
	private Text description;

	@Inject
	private MDirtyable dirty;

	@Inject
	private ITodoModel model;

	@Inject
	private Shell shell;

	private final class MyDirtyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			if (dirty != null) {
				dirty.setDirty(true);
			}
		}
	}

	private MyDirtyListener myDirtyListener = new MyDirtyListener();
	private DateTime dateTime;
	private Button btnDone;

	public TodoDetailsPart() {
		System.out.println(getClass());
	}

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Label lblSummary = new Label(parent, SWT.NONE);
		lblSummary.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSummary.setText("Summary");

		summary = new Text(parent, SWT.BORDER);
		summary.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent arg0) {
				if (todo == null)
					todo = new Todo();
				todo.setSummary(summary.getText());
			}
		});
		summary.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false,
				1, 1));
		summary.addModifyListener(myDirtyListener);

		Label lblDescription = new Label(parent, SWT.NONE);
		lblDescription.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblDescription.setText("Description");

		description = new Text(parent, SWT.BORDER | SWT.MULTI);
		GridData gd_description = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_description.heightHint = 107;
		description.setLayoutData(gd_description);
		description.addModifyListener(myDirtyListener);

		final ControlDecoration deco = new ControlDecoration(description,
				SWT.TOP | SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		deco.setDescriptionText("I'm a info decoration");
		deco.setImage(image);

		deco.setShowOnlyOnFocus(false);

		description.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (todo == null)
					todo = new Todo();
				Text text = (Text) e.getSource();
				if (text.getText().length() > 0) {
					deco.hide();
					todo.setDescription(description.getText());
				} else
					deco.show();

			}
		});
		description.addListener(SWT.Modify, this);

		// Help the user with the possible inputs
		char[] autoActivationCharacters = new char[] { '.', '#' };
		KeyStroke keyStroke;

		try {
			keyStroke = KeyStroke.getInstance("Ctrl+Space");
			ContentProposalAdapter adapter = new ContentProposalAdapter(
					description, new TextContentAdapter(),
					new SimpleContentProposalProvider(new String[] {
							"ProposalOne", "ProposalTwo", "ProposalThree" }),
					keyStroke, autoActivationCharacters);

		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Label lblDueDate = new Label(parent, SWT.NONE);
		lblDueDate.setText("Due Date");

		dateTime = new DateTime(parent, SWT.BORDER);
		dateTime.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false,
				1, 1));
		dateTime.addListener(SWT.Modify, this);
		new Label(parent, SWT.NONE);

		btnDone = new Button(parent, SWT.CHECK);
		btnDone.setText("Done");
		updateUserInterface(todo);
		m_bindingContext = initDataBindings();

	}

	@Focus
	private void setFocus() {
		summary.setFocus();
	}

	@Override
	public void handleEvent(Event arg0) {
		// TODO Auto-generated method stub
		// save();
	}

	@Persist
	private void save(ITodoModel model) {
		model.saveTodo(todo);
		dirty.setDirty(false);
	}

	@Inject
	public void setTodo(
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Todo todo) {
		if (todo != null) {

			if (dirty != null && dirty.isDirty()) {
				boolean res = MessageDialog.openConfirm(shell,
						"Save Todo Changes", "Save changes from todo: "
								+ this.todo.getSummary());
				if (res) {
					model.saveTodo(this.todo);
				}

			}

			this.todo = todo;

			updateUserInterface(todo);

		}
	}

	private void updateUserInterface(Todo todo) {
		if (todo == null) {
			return;
		}
		m_bindingContext.dispose();
		if (summary != null && !summary.isDisposed()) {

			m_bindingContext = initDataBindings();

			if (dirty != null)
				dirty.setDirty(false);
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextSummaryObserveWidget = WidgetProperties
				.text(SWT.Modify).observe(summary);
		IObservableValue summaryTodoObserveValue = PojoProperties.value(
				"summary").observe(todo);
		bindingContext.bindValue(observeTextSummaryObserveWidget,
				summaryTodoObserveValue, null, null);
		//
		IObservableValue observeTextDescriptionObserveWidget = WidgetProperties
				.text(SWT.Modify).observe(description);
		IObservableValue descriptionTodoObserveValue = PojoProperties.value(
				"description").observe(todo);
		bindingContext.bindValue(observeTextDescriptionObserveWidget,
				descriptionTodoObserveValue, null, null);
		//
		IObservableValue observeSelectionBtnDoneObserveWidget = WidgetProperties
				.selection().observe(btnDone);
		IObservableValue doneTodoObserveValue = PojoProperties.value("done")
				.observe(todo);
		bindingContext.bindValue(observeSelectionBtnDoneObserveWidget,
				doneTodoObserveValue, null, null);
		//
		return bindingContext;
	}
}
