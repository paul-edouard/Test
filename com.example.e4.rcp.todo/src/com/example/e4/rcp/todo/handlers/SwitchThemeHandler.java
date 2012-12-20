package com.example.e4.rcp.todo.handlers;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.css.swt.theme.IThemeEngine;

public class SwitchThemeHandler {

	// Remember the state
	static boolean defaulttheme = true;

	@Execute
	public void execute(
			IThemeEngine engine,
			@Named("com.example.e4.rcp.todo" + ".commandparameter.red") String red,
			@Named("com.example.e4.rcp.todo" + ".commandparameter.default") String def) {
		if (!defaulttheme) {
			engine.setTheme(def, true);
		} else {
			engine.setTheme(red, true);
		}
		defaulttheme = !defaulttheme;

	}

}