package com.osscameroon;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import com.osscameroon.service.JsElementService;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String sampleHtml = "" + "<!-- Button trigger modal -->\n"
				+ "<button type=\"button\" class=\"btn btn-primary\" data-toggle=\"modal\" data-target=\"#exampleModal\">\n"
				+ "  Launch demo modal\n" + "</button>\n" + "\n" + "<!-- Modal -->\n"
				+ "<div class=\"modal fade\" id=\"exampleModal\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"exampleModalLabel\" aria-hidden=\"true\">\n"
				+ "  <div class=\"modal-dialog\" role=\"document\">\n" + "    <div class=\"modal-content\">\n"
				+ "      <div class=\"modal-header\">\n"
				+ "        <h5 class=\"modal-title\" id=\"exampleModalLabel\">Modal title</h5>\n"
				+ "        <button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-label=\"Close\">\n"
				+ "          <span aria-hidden=\"true\">&times;</span>\n" + "        </button>\n" + "      </div>\n"
				+ "      <div class=\"modal-body\">\n" + "        ...\n" + "      </div>\n"
				+ "      <div class=\"modal-footer\">\n"
				+ "        <button type=\"button\" class=\"btn btn-secondary\" data-dismiss=\"modal\">Close</button>\n"
				+ "        <button type=\"button\" class=\"btn btn-primary\">Save changes</button>\n" + "      </div>\n"
				+ "    </div>\n" + "  </div>\n" + "</div>";

		Element doc = Jsoup.parse(sampleHtml, "", Parser.xmlParser());
		System.out.println(JsElementService.parseElement(doc));

	}

}
