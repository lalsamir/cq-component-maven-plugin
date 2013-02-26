package com.citytechinc.cq.component.editconfig.factory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;

import com.citytechinc.cq.component.annotations.Component;
import com.citytechinc.cq.component.dialog.exception.InvalidComponentClassException;
import com.citytechinc.cq.component.editconfig.EditConfig;
import com.citytechinc.cq.component.editconfig.impl.SimpleEditConfig;

public class EditConfigFactory {

	public static final EditConfig make(Class<?> componentClass) throws InvalidComponentClassException {

		Component componentAnnotation = componentClass.getAnnotation(Component.class);

		if (!(componentAnnotation instanceof Component)) {
			throw new InvalidComponentClassException("Class provided is not property annotated");
		}

		String title = getTitleForEditConfig(componentClass, componentAnnotation);
		List<String> actions = getActionsForEditConfig(componentAnnotation, title);
		String dialogMode = getDialogModeForEditConfig(componentAnnotation);
		String layout = getLayoutForEditConfig(componentAnnotation);

		return new SimpleEditConfig(title, actions, dialogMode, layout, "cq:EditConfig");
	}

	private static final String getTitleForEditConfig(Class<?> componentClass, Component componentAnnotation) {
		if (StringUtils.isNotEmpty(componentAnnotation.title())) {
			return componentAnnotation.title();
		}

		return componentClass.getSimpleName();
	}

	private static final List<String> getActionsForEditConfig(Component componentAnnotation, String title) {
		List<String> actions = Arrays.asList(componentAnnotation.actions());

		if (!actions.isEmpty()) {
			return actions;
		}

		actions = new ArrayList<String>();

		actions.add("text:" + title);
		actions.add("-");
		actions.add("edit");
		actions.add("copymove");
		actions.add("delete");
		actions.add("-");
		actions.add("insert");

		return actions;
	}

	private static final String getDialogModeForEditConfig(Component componentAnnotation) {
		if (StringUtils.isNotEmpty(componentAnnotation.dialogMode())) {
			return componentAnnotation.dialogMode();
		}

		return "floating";
	}

	private static final String getLayoutForEditConfig(Component componentAnnotation) {
		if (StringUtils.isNotEmpty(componentAnnotation.layout())) {
			return componentAnnotation.layout();
		}

		return "editbar";
	}
}