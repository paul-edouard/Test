package com.example.e4.rcp.todo.contribute.processors;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MDirectMenuItem;
import org.eclipse.e4.ui.model.application.ui.menu.MMenu;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuElement;
import org.eclipse.e4.ui.model.application.ui.menu.MMenuFactory;

public class MenuProcessor {

	@Inject
	@Named("org.eclipse.ui.file.menu")
	private MMenu menu;

	@Execute
	public void execute() {
		System.out.println("Starting processor");
		if (menu != null && menu.getChildren() != null) {

			List<MMenuElement> list = new ArrayList<MMenuElement>();
			for (MMenuElement element : menu.getChildren()) {

				System.out.println(element.getLabel());
				if (element.getLabel() != null) {
					if (element.getLabel().contains("Exit")) {

						// TODO Noch tiefer gehen
						System.out.println("Exit Found: " + element.getLabel());
						list.add(element);

					}
				}

			}

			menu.getChildren().removeAll(list);

		}

		// TODO Processor
		MDirectMenuItem menuItem = MMenuFactory.INSTANCE.createDirectMenuItem();
		menuItem.setLabel("New Exit");
		menuItem.setContributionURI("bundleclass://com.example.e4.rcp.todo.contribute/com.example.e4.rcp.todo.contribute.handler.ExitHandlerWithCheck");
		menu.getChildren().add(menuItem);
	}

}
