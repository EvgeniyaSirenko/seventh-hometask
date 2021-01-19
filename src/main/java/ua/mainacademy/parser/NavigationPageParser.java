package ua.mainacademy.parser;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.mainacademy.model.Item;

import java.util.ArrayList;
import java.util.List;

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
		/*
		TODO: extract item links
		 */

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
			if (threads.size() > 2) {
				return;
			}
			int lastPage = extractLastPage(document);
			for (int i = 1; i <= lastPage; i++) {
				String nextPageUrl = url + "&page=" + i;
				RouterParser routerParser = new RouterParser(items, threads, nextPageUrl);
				threads.add(routerParser);
				routerParser.start();
			}
		}
	}

	private int extractLastPage(Document document) {
		Elements resultElements = document.getElementsByClass(
				"page-link pagination__link js-pagination__link-last js-pagination_link");
		String result = resultElements.first().text();
		return Integer.valueOf(result);
	}

	public static boolean isNavigationPage(String url) {
		return !url.contains("/product/");
	}
}
