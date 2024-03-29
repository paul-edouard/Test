package com.example.e4.rcp.todo.handlers;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class PerpectiveSwitcherHandler {
	@Execute
	public void switchperpective(MPerspective activePerspective,
			MApplication app, EPartService partService,
			EModelService modelService) {

		List<MPerspective> perspectives = modelService.findElements(app, null,
				MPerspective.class, null);
		// Assume only to classes
		for (MPerspective perspective : perspectives) {
			if (!perspective.equals(activePerspective)) {
				partService.switchPerspective(perspective);
			}
		}

	}

}