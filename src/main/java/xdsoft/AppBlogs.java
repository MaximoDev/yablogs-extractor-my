package xdsoft;

//java -cp "hadoop.jar:lib/*" xdsoft.AppBlogs

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by serge on 09.04.15.
 */
public class AppBlogs {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		File config_file = new File("config.txt");

		try {
			prop.load(new FileReader(config_file));
		} catch (Exception e1) {
			System.out.println("Неверно заполнен файл настроек config.txt");
			return;
		}

		String myxml = prop.getProperty("myxml");
		String appContext = prop.getProperty("applicationContext");

		if (myxml == null || appContext == null) {
			System.out.println("Неверно заполнен файл настроек config.txt");
			return;
		}

		System.out.println("--- Properties ---");
		System.out.println("config_file: " + config_file.getAbsolutePath());
		System.out.println("myxml: " + myxml);
		System.out.println("appContext: " + appContext);
		System.out.println("------------------");

		@SuppressWarnings("resource")
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(appContext);

		YablogsExctractor exctractor = (YablogsExctractor) applicationContext.getBean("yablogs-extractor");
		try {
			exctractor.exctract(myxml);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
