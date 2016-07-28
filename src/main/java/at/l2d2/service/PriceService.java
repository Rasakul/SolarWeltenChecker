package at.l2d2.service;

import at.l2d2.entity.Product;
import com.opencsv.CSVReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;

/**
 * <h4>About this class</h4>
 * <p>
 * <p>Description</p>
 *
 * @author Lukas Baronyai
 * @version 1.0.0
 * @since 13.07.2016
 */
@SuppressWarnings("Duplicates")
public class PriceService {
	//private final String path = "D:\\Benutzer\\Lukas\\workspace\\Java\\SolarWeltenChecker\\products.csv";
	private CSVReader csvReader;
	
	//	public List<Product> loadAllProducts() {
	//
	//		List<Product> products = new ArrayList<>();
	//		List<String[]> result = readFile();
	//		if (result != null && result.size() > 0) {
	//			products.addAll(
	//					result.stream()
	//					      .map(line -> loadProduct(line[1], line[0], line[2]))
	//					      .collect(Collectors.toList()));
	//		}
	//
	//		return products;
	//	}
	
	public List<String[]> readFile() {
		
		try {
			
			String path = PriceService.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String decodedPath = URLDecoder.decode(path, "UTF-8");
			path = new File(decodedPath).getParentFile().getPath();
			path += File.separator + "products.csv";
			
			csvReader = new CSVReader(new FileReader(path), ';');
			return csvReader.readAll();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Product loadProduct(String ASIN, String name, String realName) {
		
		try {
			System.out.println("load: " + "https://www.amazon.de/dp/" + ASIN);
			String amazonPrice = loadAmazonPrice(ASIN);
			System.out.println(amazonPrice);
			
			System.out.println("load: " + "http://www.solarwelten.de/produkt/" + name);
			String solarPrice = loadSolarwelten(name);
			System.out.println(solarPrice);
			
			return new Product(ASIN, realName, amazonPrice, solarPrice);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String loadAmazonPrice(String ASIN) throws IOException {
		Document doc = Jsoup.connect("https://www.amazon.de/dp/" + ASIN)
		                    .data("query", "Java")
		                    .userAgent("Mozilla")
		                    .cookie("auth", "token")
		                    .timeout(30000)
		                    .post();
		
		Elements price_elem = doc.select("#priceblock_saleprice");
		String price;
		
		if (price_elem.size() > 0) {
			price = price_elem.first().text().replace("EUR ", "");
		} else {
			price_elem = doc.select("#priceblock_ourprice");
			
			if (price_elem.size() > 0) price = price_elem.first().text().replace("EUR ", "");
			else price = "Nicht verfügbar";
		}
		
		return price;
	}
	
	private String loadSolarwelten(String name) throws IOException {
		Document doc = Jsoup.connect("http://www.solarwelten.de/produkt/" + name + "/")
		                    .data("query", "Java")
		                    .userAgent("Mozilla")
		                    .cookie("auth", "token")
		                    .timeout(3000)
		                    .post();
		
		Elements price_elem = doc.select("p.price > span.electro-price > span.woocommerce-Price-amount");
		String price;
		
		if (price_elem.size() > 0) {
			price = price_elem.first().text().replace("€", "");
		} else {
			price_elem = doc.select("#priceblock_ourprice");
			
			if (price_elem.size() > 0) price = price_elem.first().text().replace("EUR ", "");
			else price = "Nicht verfügbar";
		}
		
		return price;
	}
}
