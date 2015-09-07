package xdsoft;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BlogXMLParser {

	public static ArrayList<HashMap<String,String>> getBlogFromXML(String source)
			throws ParserConfigurationException, SAXException, IOException {

		ArrayList<HashMap<String, String>> records = new ArrayList<>();

		DocumentBuilderFactory factory;
		DocumentBuilder documentBuilder;
		Document document;

		try {

			factory = DocumentBuilderFactory.newInstance();
			documentBuilder = factory.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(source));
			document = documentBuilder.parse(is);

		} catch (Exception e) {
			System.out.println("!!!Твою дивизию, нас забанили!!!");
			throw e;
		}

		NodeList nodeList = document.getElementsByTagName("item");
		System.out.println(nodeList.toString());

		for (int i = 0; i < nodeList.getLength(); i++) {

			System.out.println(nodeList.getLength() + " - " + nodeList.toString());
			Element elem = (Element) nodeList.item(i);
			
			// link - ключевое поле
			if ((elem.getElementsByTagName("link").getLength() == 0))
				continue;

			HashMap<String, String> record = new HashMap<>();

			record.put("link", elem.getElementsByTagName("link").item(0).getTextContent());

			// author
			if (elem.getElementsByTagName("author").getLength() > 0)
				record.put("author", elem.getElementsByTagName("author").item(0).getTextContent());

			// pubDate
			if (elem.getElementsByTagName("pubDate").getLength() > 0)
				record.put("pubDate", elem.getElementsByTagName("pubDate").item(0).getTextContent());

			// title
			if (elem.getElementsByTagName("title").getLength() > 0)
				record.put("title", elem.getElementsByTagName("title").item(0).getTextContent());

			// description
			if (elem.getElementsByTagName("description").getLength() > 0)
				record.put("description", elem.getElementsByTagName("description").item(0).getTextContent());

			records.add(record);

			System.out.println(" >>>> " + record.toString());

		}
		return records;
	}

}
