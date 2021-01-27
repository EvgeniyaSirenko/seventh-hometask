package ua.mainacademy.parser;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.mainacademy.model.Item;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@AllArgsConstructor
public class NavigationPageParser extends Thread {
	private List<Item> items;
	private List<Thread> threads;
	private Document document;
	private String url;

	@Override
	public void run() {
		parsePage(url);
	}

	private void parsePage(String url) {
		//extract item links
		List<String> itemLinks = new ArrayList<>();
		Elements elements = document.getElementsByClass("products_wrapper").first()
				.getElementsByClass("product_item");

		int counter = 0;
		for (Element element : elements) {
			if (counter > 3) {
				continue;
			}
			RouterParser routerParser = new RouterParser(items, threads, element.absUrl("data-url"));
			threads.add(routerParser);
			routerParser.start();
			counter++;
		}

		for (String itemLink : itemLinks) {
			if (threads.size() > 5) {
				continue;
			}
			RouterParser routerParser = new RouterParser(items, threads, itemLink);
			threads.add(routerParser);
			routerParser.start();
		}

		//pagination
		if (!url.contains("&page=")) {
//			if (threads.size() > 2) {
//				return;
// 			no need to do this
//			}
			int lastPage = extractLastPage(document);
			for (int i = 2; i <= lastPage; i++) {
				String nextPageUrl = url + "&page=" + i;
				RouterParser routerParser = new RouterParser(items, threads, nextPageUrl);
				threads.add(routerParser);
				routerParser.start();
			}
		}
	}

	private int extractLastPage(Document document) {
		Elements resultElements = document.getElementsByClass(
				"pagination__item_last").first().getElementsByTag("a");
		String result = resultElements.first().attr("data-page");
		if(isBlank(result)) {
			return 0;
		}
		return Integer.parseInt(result);
	}

	public static boolean isNavigationPage(String url) {
		return !url.contains("/product/");
	}
}
