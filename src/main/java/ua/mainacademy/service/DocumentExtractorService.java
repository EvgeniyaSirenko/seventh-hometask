package ua.mainacademy.service;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentExtractorService {
	public static Document getDocument(String url) {
		try {
			Connection connection = Jsoup.connect(url);
			return connection.get();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Document was not downloaded");
	}
}
