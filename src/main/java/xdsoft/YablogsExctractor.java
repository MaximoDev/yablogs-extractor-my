package xdsoft;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.xml.sax.SAXException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

/**
 * Тут все и происходит.
 */
public class YablogsExctractor {

	private String path;
	private String geo;
	private String fromDay;
	private String fromMonth;
	private String fromYear;
	private String searchUrl;
	private String sourceFileXml;
	private Map<String, String> translate;

	// делаем, все, что нужно для щястья
	protected void exctract(String myxml)
			throws IOException, InterruptedException, ParserConfigurationException, SAXException {

		HttpClient client = HttpClientBuilder.create().build();

		XStream xstream = new XStream(new StaxDriver());
		xstream.alias("maincategory", MainCategory.class);
		xstream.alias("category", Categories.class);
		xstream.alias("requests", Requests.class);

		File fileMyXML = new File(myxml);
		BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(fileMyXML), "UTF-8"));

		// BufferedReader fis = new BufferedReader(new InputStreamReader(
		// new FileInputStream(
		// new File(System.getProperty("user.dir") + "/" + this.getPath() +
		// this.getSourceFileXml())),
		// "UTF-8"));

		MainCategory mainCategoryList = (MainCategory) xstream.fromXML(fis);

		for (Categories setcat : mainCategoryList.getCatgorylist()) {

			for (Requests setreq : setcat.getRequestslist()) {

				StringBuilder mynewurl = new StringBuilder();

				mynewurl.append(this.getSearchUrl());

				mynewurl.append(URLEncoder.encode(setreq.getUrlrequest().replaceAll("and", "&").trim(), "UTF-8"));
				mynewurl.append("&geo=" + this.getGeo() + "&from_day=" + this.getFromDay() + "&from_month="
						+ this.getFromMonth() + "&from_year=" + this.getFromYear());

				HttpGet req = new HttpGet(mynewurl.toString());
				HttpResponse resp = client.execute(req);

				String sourceXML = EntityUtils.toString(resp.getEntity(), "UTF-8");

				ArrayList<HashMap<String, String>> recordsToHBase = BlogXMLParser.getBlogFromXML(sourceXML);
				if (!recordsToHBase.isEmpty())
					HBaseWriter.writeToHBase("yablogs", "yablogs", "link", recordsToHBase);
				else
					System.out.println("SKIPPED!!!");

				Thread.sleep(2000L);
			}
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getGeo() {
		return geo;
	}

	public void setGeo(String geo) {
		this.geo = geo;
	}

	public String getFromDay() {
		return fromDay;
	}

	public void setFromDay(String fromDay) {
		this.fromDay = fromDay;
	}

	public String getFromMonth() {
		return fromMonth;
	}

	public void setFromMonth(String fromMonth) {
		this.fromMonth = fromMonth;
	}

	public String getFromYear() {
		return fromYear;
	}

	public void setFromYear(String fromYear) {
		this.fromYear = fromYear;
	}

	public String getSearchUrl() {
		return searchUrl;
	}

	public void setSearchUrl(String searchUrl) {
		this.searchUrl = searchUrl;
	}

	public String getSourceFileXml() {
		return sourceFileXml;
	}

	public void setSourceFileXml(String sourceFileXml) {
		this.sourceFileXml = sourceFileXml;
	}

	public Map<String, String> getTranslate() {
		return translate;
	}

	public void setTranslate(Map<String, String> translate) {
		this.translate = translate;
	}

}
