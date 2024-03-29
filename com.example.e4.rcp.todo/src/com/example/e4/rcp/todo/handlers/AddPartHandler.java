package com.example.e4.rcp.todo.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

public class AddPartHandler {
	@Execute
	public void execute(EPartService service) {
		MPart part = service
				.createPart("com.example.e4.rcp.todo.partdescriptor.dynamic");

		service.showPart(part, PartState.ACTIVATE);

	}

}