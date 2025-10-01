package com.Exchange.Exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.awt.*;
import java.net.URI;


@SpringBootApplication
public class ExchangeApplication {

	/*
	1 - Добавить биржи
	(BingX, Huobi, gate.io, bitfinex, bitmart)
    */
	/*
	для добавления биржи:
	1 - ExportFile_Controller
	2 - SymbolCheck_Controller
	3 - TickersTable_Controller
	4 - TopTickers_Controller
	 */

	private static ApplicationContext context;
	private volatile boolean shouldShutdown = false;

	public static void main(String[] args) {
		System.setProperty("server.port", "8081");
		context = SpringApplication.run(ExchangeApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void openBrowser() {
		String url = "http://localhost:8081";

		try {
			if (Desktop.isDesktopSupported()) {
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Desktop.Action.BROWSE)) {
					desktop.browse(new URI(url));
					System.out.println("Браузер открыт: " + url);
				} else {
					System.out.println("Браузер не поддерживается. Откройте вручную: " + url);
				}
			} else {
				String os = System.getProperty("os.name").toLowerCase();
				if (os.contains("win")) {
					Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
				} else if (os.contains("mac")) {
					Runtime.getRuntime().exec("open " + url);
				} else if (os.contains("nix") || os.contains("nux")) {
					Runtime.getRuntime().exec("xdg-open " + url);
				}
				System.out.println("Попытка открыть браузер: " + url);
			}
		} catch (Exception e) {
			System.err.println("Не удалось открыть браузер автоматически: " + e.getMessage());
			System.out.println("Откройте браузер вручную и перейдите по адресу: " + url);
		}
	}

	public void initiateShutdown() {
		System.out.println("Инициировано завершение работы приложения...");
		new Thread(() -> {
			try {
				Thread.sleep(2000);
				SpringApplication.exit(context, () -> 0);
				System.exit(0);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}).start();
	}
}