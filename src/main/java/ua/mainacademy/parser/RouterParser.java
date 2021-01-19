package ua.mainacademy.parser;

import lombok.AllArgsConstructor;
import org.jsoup.nodes.Document;
import ua.mainacademy.model.Item;
import ua.mainacademy.service.DocumentExtractorService;

import java.util.List;

@AllArgsConstructor
public class RouterParser extends Thread {
	private List<Item> items;
	private List<Thread> threads;
	private String url;


	@Override
	public void run() {
		parsePage(url);
	}

	private void parsePage(String url) {
		Document document = DocumentExtractorService.getDocument(url);
		if (ItemPageParser.isItemPage(url)) {
			ItemPageParser itemPageParser = new ItemPageParser(items, document, url);
			threads.add(itemPageParser);
			itemPageParser.start();
		}
		if (NavigationPageParser.isNavigationPage(url)) {
			NavigationPageParser navigationPageParser =
					new NavigationPageParser(items, threads, document, url);
			navigationPageParser.start();
		}
	}
}
