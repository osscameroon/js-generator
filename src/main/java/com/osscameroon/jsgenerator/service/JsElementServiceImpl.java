package com.osscameroon.jsgenerator.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;

import com.osscameroon.jsgenerator.model.JsElement;

/**
 *
 * JsElementService class is used as a driver for converting the Jsoup Elements
 * to JsElement appended strings.
 *
 * @author osscameroon
 */
public class JsElementServiceImpl implements JsElementService {

	private static final List<String> usedTags = new ArrayList<>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String parseElement(Element element) {
		StringBuilder generatedCode = new StringBuilder();

		for (Element child : element.children()) {
			generatedCode.append(parseElement(child)).append("\n"); // recursive

			JsElement parent = new JsElement(child);

			generatedCode.append(parent.parse(usedTags)); // parse this current element

			String appends = parent.appendChild(); // append this current element's children code to parent code

			if (!appends.equals(""))
				generatedCode.append(appends);
		}
		return generatedCode.toString();
	}

}
