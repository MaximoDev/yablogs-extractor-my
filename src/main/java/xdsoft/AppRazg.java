package xdsoft;

// java -cp "hadoop.jar:lib/*" xdsoft.AppRazg

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class AppRazg {

	public static void main(String[] args)
			throws ClientProtocolException, IOException, ParserConfigurationException, SAXException {

		// готовим URL
		StringBuilder urlB = new StringBuilder();
		String urlS = "http://mobile.navi.yandex.net/" + "userpoi/getpoints?uuid=12345678901234567890&" + "zoom=6&"
				+ "tl_lat=55.882954&" // top left широта
				+ "tl_lon=37.39108&" // top left долгота
				+ "br_lat=55.592408&" // bottom right широта
				+ "br_lon=37.842888&" // bottom right долгота
				+ "catlist=6&" + "rawcatlist=&" + "rawpointsformat=full&" + "ver=2&" + "utf&" + "lang=ru-RU";

		urlB.append(urlS);

		// Читаем данные из потока HTTP запроса, формируем String с содержимым
		// ответа (XML)
		URL newUrl = new URL(urlB.toString());
		URLConnection yc = newUrl.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuilder responceSB = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			responceSB.append(inputLine);
		}
		in.close();
		String responceSTR = responceSB.toString();

		// temp
		// PrintWriter pf = new PrintWriter(new File("ya-razgovorchiki.xml"));
		// pf.write(responceSTR);
		// pf.close();

		// Читаем данные вытянутого XML через модель DOM
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();

		InputSource is = new InputSource(new StringReader(responceSTR));
		Document doc = builder.parse(is);

		ArrayList<HashMap<String, String>> hbaseSet = new ArrayList<>();

		NodeList talkList = doc.getElementsByTagName("wpt");
		for (int i = 0; i < talkList.getLength(); i++) {

			Node t = talkList.item(i);
			if (t.getNodeType() == Node.ELEMENT_NODE) {

				HashMap<String, String> hbaseRecord = new HashMap<>();

				Element talk = (Element) t;
				hbaseRecord.put("lat", talk.getAttribute("lat"));
				hbaseRecord.put("lon", talk.getAttribute("lon"));
				hbaseRecord.put("catidx", talk.getAttribute("catidx"));
				hbaseRecord.put("point_id", talk.getAttribute("point_id"));

				Locale local = new Locale("ru", "RU");
				DateFormat df = DateFormat.getDateInstance(DateFormat.DEFAULT, local);
				Date currentDate = new Date();

				NodeList talkDetails = talk.getChildNodes();
				for (int y = 0; y < talkDetails.getLength(); y++) {

					Node d = talkDetails.item(y);
					if (d.getNodeType() == Node.ELEMENT_NODE) {
						Element detail = (Element) d;
						switch (detail.getTagName()) {
						case "name":
							hbaseRecord.put("name", detail.getTextContent());
							hbaseRecord.put("timeday",
									df.format(currentDate) + " " + detail.getTextContent().substring(0, 5));
							break;
						case "comment":
							hbaseRecord.put("comment", detail.getTextContent());
							break;
						case "time":
							hbaseRecord.put("time", detail.getTextContent());
							break;
						}
					}
				}
				// String rec = "lat:" + lat + " | lon:" + lon + " | catidx:" +
				// catidx + " | timeday:" + timeday
				// + " | name:" + name + " | comment:" + comment + " | time:" +
				// time + " | point_id:" + point_id;
				// System.out.println(rec);

				hbaseSet.add(hbaseRecord);

			}
		}

		if (!hbaseSet.isEmpty())
			HBaseWriter.writeToHBase("razgovorchiki", "razgovorchiki", "point_id", hbaseSet);
		else
			System.out.println("SKIPPED!!!");
	}
}
