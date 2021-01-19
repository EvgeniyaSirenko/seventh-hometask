package ua.mainacademy;

import ua.mainacademy.model.Item;
import ua.mainacademy.parser.RouterParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class AppRunner {
	private static final Logger LOG = Logger.getLogger(AppRunner.class.getName());

	public static void main(String[] args) {
		String keyWord = args.length == 0 ? "фольга алюминиевая" : args[0];
		String url = "https://parfums.ua";
		String searchUrl = url + "/search/show?q=" + keyWord.replaceAll(" ", "%20");

		List<Item> items = Collections.synchronizedList(new ArrayList<>());
		List<Thread> threads = Collections.synchronizedList(new ArrayList<>());

		RouterParser routerParser = new RouterParser(items, threads, searchUrl);
		threads.add(routerParser);
		routerParser.start();
		do {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!threadsAreNotActive(threads));

		LOG.info(String.format("Items were extracted. Amount = %d", items.size()));
		//System.out.println( "Items were extracted. Amount = " + items.size());
	}

	private static boolean threadsAreNotActive(List<Thread> threads) {
		for (Thread thread : threads) {
			if (thread.isAlive() || thread.getState().equals(Thread.State.NEW)) {
				return false;
			}
		}
		return  true;
	}
}
