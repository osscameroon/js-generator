package jsgenerator.selfclosingtagissue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

public class SelfClosingTagIssue {

    public static void main(String[] args) {

	String selfClosingTagWorking = "<input type=\"text\"/>\r\n" + "<img src=\"#URL\" alt=\"image\">";

	String selfClosingTagNotWorking = "<input type=\"text\">\r\n" + "<img src=\"#URL\" alt=\"image\">";

	String div = "<br> <div><div/>";

	Element htmlDoc = Jsoup.parse(selfClosingTagNotWorking, "", Parser.htmlParser());

		analyzeElement(htmlDoc);

	}

	private static void analyzeElement(Element htmlDoc) {
		for (Element child : htmlDoc.children()) {

			boolean hasChild = child.childrenSize() > 0;

			System.err.println(" **** Analyze :" + child.tag().getName() + " -> isEmpty : " + child.tag().isEmpty()
				+ " -> isSelfClosing : " + child.tag().isSelfClosing() + " -> hasChild : " + hasChild + " **** ");

			if (hasChild) {
			System.err.println("Children of " + child.tagName() + ": ");

			for (Element e : child.children()) {

				System.err.println(e.tagName());

			}
			}

			analyzeElement(child);

		}
	}

}
