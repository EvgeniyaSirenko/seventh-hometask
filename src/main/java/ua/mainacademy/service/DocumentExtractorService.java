package ua.mainacademy.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Map;

public class DocumentExtractorService {
	public static Document getDocument(String url) {
		try {
			Connection connection = Jsoup.connect(url);
			connection
					.headers(
							Map.of("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
									"Accept-Encoding", "gzip, deflate, br",
									"Accept-Language", "ru",
									"User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0.2 Safari/605.1.15"
							)
					);
			return connection.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Document was not downloaded");
	}
}
