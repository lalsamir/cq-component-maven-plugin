/**
 *    Copyright 2013 CITYTECH, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.citytechinc.cq.component.touchuidialog.widget.registry;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javassist.ClassPool;
import javassist.NotFoundException;

import org.codehaus.plexus.util.StringUtils;
import org.reflections.Reflections;

import com.citytechinc.cq.component.maven.util.ComponentMojoUtil;
import com.citytechinc.cq.component.util.TouchUIWidgetConfigHolder;

public class DefaultTouchUIWidgetRegistry implements TouchUIWidgetRegistry {
	private static final String DEFAULT_COMPONENT_PACKAGE = "com.citytechinc.cq.component.touchuidialog";

	private final Map<Class<?>, TouchUIWidgetConfigHolder> annotationToWidgetConfigMap;

	public DefaultTouchUIWidgetRegistry(ClassPool classPool, ClassLoader classLoader, Reflections reflections,
		List<String> additionalFeatures) throws NotFoundException, ClassNotFoundException {
		annotationToWidgetConfigMap = new HashMap<Class<?>, TouchUIWidgetConfigHolder>();

		List<TouchUIWidgetConfigHolder> widgetConfigurations =
			ComponentMojoUtil.getAllTouchUIWidgetAnnotations(classPool, classLoader, reflections);

		Collections.sort(widgetConfigurations, new Comparator<TouchUIWidgetConfigHolder>() {

			@Override
			public int compare(TouchUIWidgetConfigHolder widgetConfig1, TouchUIWidgetConfigHolder widgetConfig2) {
				if (widgetConfig1.getWidgetClass().getName().startsWith(DEFAULT_COMPONENT_PACKAGE)) {
					return -1;
				} else {
					return 1;
				}
			}
		});

		for (TouchUIWidgetConfigHolder currentWidgetConfiguration : widgetConfigurations) {
			if (currentWidgetConfiguration.getAnnotationClass() != null
				&& (StringUtils.isEmpty(currentWidgetConfiguration.getFeatureFlag()) || additionalFeatures
					.contains(currentWidgetConfiguration.getFeatureFlag()))) {
				annotationToWidgetConfigMap.put(currentWidgetConfiguration.getAnnotationClass(),
					currentWidgetConfiguration);
			}
		}
	}

	@Override
	public TouchUIWidgetConfigHolder getWidgetForAnnotation(Class<?> annotation) {
		return annotationToWidgetConfigMap.get(annotation);
	}

	@Override
	public Set<Class<?>> getRegisteredAnnotations() {
		return annotationToWidgetConfigMap.keySet();
	}

}
