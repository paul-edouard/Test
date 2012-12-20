package com.example.e4.rcp.todo.ui.parts;

import java.net.URLEncoder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.example.e4.rcp.todo.events.MyEventConstants;
import com.example.e4.rcp.todo.model.Todo;

public class PlaygroundPart {
	private Text text;
	private Browser browser;

	public PlaygroundPart() {
		System.out.println(getClass());
	}

	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		text = new Text(parent, SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Button btnGoToCity = new Button(parent, SWT.NONE);
		btnGoToCity.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					browser.setUrl("http://maps.google.de/maps?q="
							+ URLEncoder.encode(text.getText(), "UTF-8"));
				} catch (Exception ex) {
					System.out.println(ex);
				}

			}
		});
		btnGoToCity.setText("Go to City...");

		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2,
				1));

	}

	@Focus
	private void setFocus() {
		text.setFocus();
	}

	@Inject
	@Optional
	public void trackUserSettingschanges(
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "user") String user,
			@Preference(nodePath = "com.example.e4.rcp.todo", value = "password") String password) {
		System.out.println("user: " + user);
		System.out.println("password: " + password);
	}

	@Inject
	@Optional
	public void reactToTodoDeletion(
			@UIEventTopic(MyEventConstants.TOPIC_TODO_DATA_UPDATE) Todo todo) {
		System.out.println("Delete ToDo: " + todo.getDescription());

	}
}
