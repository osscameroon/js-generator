/*
 * 
 */
package com.osscameroon.jsGenerator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.osscameroon.jsGenerator.service.JsElementService;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		StringBuilder sampleHtml = new StringBuilder();

		sampleHtml.append("<!-- Button trigger modal -->\n")
					.append("<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#exampleModal\">\n")
					.append("  Launch demo modal\n")
					.append("</button>\n")
					.append("\n")
					.append("<!-- Modal -->\n")
					.append("<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n")
					.append("  <div class=\"modal-dialog\" role=\"document\">\n")
					.append("    <div class=\"modal-content\">\n")
					.append("      <div class=\"modal-header\">\n")
					.append("        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n")
					.append("        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n")
					.append("          <span aria-hidden=\"true\">&times;</span>\n")
					.append("        </button>\n")
					.append("      </div>\n")
					.append("      <div class=\"modal-body\">\n")
					.append("        ...\n")
					.append("      </div>\n")
					.append("      <div class=\"modal-footer\">\n")
					.append("        <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>\n")
					.append("        <button type=\"button\" class=\"btn btn-primary\">Save changes</button>\n")
					.append("      </div>\n")
					.append("    </div>\n")
					.append("  </div>\n")
					.append("</div>");

		Element doc = Jsoup.parse(sampleHtml.toString(), "", Parser.xmlParser());
		System.out.println(JsElementService.parseElement(doc));

	}

}
