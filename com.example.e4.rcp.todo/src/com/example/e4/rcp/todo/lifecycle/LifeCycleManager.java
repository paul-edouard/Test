package com.example.e4.rcp.todo.lifecycle;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.swt.widgets.Display;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

// For a extended example see
// https://bugs.eclipse.org/382224
// Also add org.eclipse.osgi.services as dependency
public class LifeCycleManager {
	@PostContextCreate
	void postContextCreate(final IEventBroker eventBroker) {

		System.out.println("PostContextCreate called");

		Display display = Display.getDefault();
		final LifeCycleShell shell = new LifeCycleShell(display);

		// final Shell shell = new Shell(SWT.TOOL | SWT.NO_TRIM);
		// Will close the shell once a part gets activated
		// Should be easier
		// See the following bug reports
		// https://bugs.eclipse.org/376821
		eventBroker.subscribe(UIEvents.UILifeCycle.ACTIVATE,
				new EventHandler() {
					@Override
					public void handleEvent(Event event) {
						shell.close();
						shell.dispose();
						System.out.println("Closed");
						eventBroker.unsubscribe(this);
					}
				});
		shell.open();

		int max = 2;
		shell.getProgressBar().setMaximum(max);
		for (int i = 0; i < max; i++) {

			try {
				Thread.sleep(200);
				shell.getProgressBar().setState(i);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
