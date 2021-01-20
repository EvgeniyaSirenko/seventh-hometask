package ua.mainacademy.parser;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ua.mainacademy.AppRunner;
import ua.mainacademy.model.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@AllArgsConstructor

public class ItemPageParser extends Thread {
	private List<Item> items;
	private Document document;
	private String url;

	public static boolean isItemPage(String url) {
		return !url.contains("/product/");
	}

	@Override
	public void run() {
		items.add(getItemFromPage(url));
	}

	private Item getItemFromPage(String url) {
		Element productBlock = document.getElementById("wrapper");

		String name = extractName(productBlock);
		int price = extractPrice(productBlock);
		int initPrice = extractInitPrice(productBlock) == 0 ? price : extractInitPrice(productBlock);
		String imageUrl = extractImageUrl(productBlock);
		String group = extractGroup(document);

		return Item.builder()
				.name(name)
				.price(price)
				.initPrice(initPrice)
				.group(group)
				.url(url)
				.imageUrl(imageUrl)
				.build();
	}

	private String extractName(Element element) {
		return element.getElementsByClass("product_name_wrap").first().getElementsByAttribute("title").first().text();
	}

	private int extractInitPrice(Element element) {
		List<Element> elementList = element.getElementsByClass("price_num");
		if (elementList.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(elementList.get(0).text().replaceAll("\\D", ""));
	}

	private static int extractPrice(Element element) {
		List<Element> elementList = element.getElementsByClass("discount_price");
		if (elementList.isEmpty()) {
			return 0;
		}
		return Integer.valueOf(elementList.get(0).text().replaceAll("\\D", ""));
	}

	private String extractImageUrl(Element element) {
		//Element imageElements = document.select("img").first();
		Elements img = document.select("div.duct_item img[src]");
		String imageUrl = img.attr("src");
		//String imageUrl = imageElements.attr("src");
		return imageUrl;
	}

	private String extractGroup(Document document) {
		//String result = "";
		Elements groupDiv = document.getElementsByClass("breadcrumbs_block");
		Elements groupElements = groupDiv.first().getElementsByTag("a");
		List<String> groups = new ArrayList<>();
		for (Element element : groupElements) {
			groups.add(element.text());
		}
		return StringUtils.join(groups, ">");
	}
}
