package com.example.e4.rcp.todo.lifecycle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

public class LifeCycleShell extends Shell {
	private ProgressBar progressBar;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			Display display = Display.getDefault();
			LifeCycleShell shell = new LifeCycleShell(display);
			shell.open();
			shell.layout();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the shell.
	 * 
	 * @param display
	 */
	public LifeCycleShell(Display display) {
		super(display, SWT.TOOL | SWT.NO_TRIM);
		setAlpha(0);
		setBackground(SWTResourceManager.getColor(51, 153, 255));

		progressBar = new ProgressBar(this, SWT.NONE);
		progressBar.setBounds(-11, 245, 445, 17);

		Label lblNewLabel = new Label(this, SWT.NONE);
		lblNewLabel.setBackground(SWTResourceManager
				.getColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 15,
				SWT.ITALIC));
		lblNewLabel.setBounds(89, 100, 200, 37);
		lblNewLabel.setText("Please Wait...");
		createContents();

	}

	/**
	 * Create contents of the shell.
	 */
	protected void createContents() {
		setText("SWT Application");
		setSize(450, 300);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

}
