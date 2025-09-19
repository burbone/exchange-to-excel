package Exchange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class BybitExelApplication {

	public static void main(String[] args) {
		SpringApplication.run(BybitExelApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void openBrowser() {
		try {
			Thread.sleep(2000);

			String port = "8081";
			String url = "http://localhost:" + port;

			System.out.println("🚀 Exchange to Excel запущен!");
			System.out.println("📱 Открываю браузер...");

			boolean browserOpened = false;

			try {
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI(url));
					browserOpened = true;
					System.out.println("✅ Браузер открыт автоматически!");
				}
			} catch (Exception e) {
				System.out.println("⚠️  Не удалось открыть браузер через Desktop API, пробую альтернативный способ...");
			}

			if (!browserOpened) {
				try {
					String os = System.getProperty("os.name").toLowerCase();
					String command;
					
					if (os.contains("win")) {
						command = "cmd /c start " + url;
					} else if (os.contains("mac")) {
						command = "open " + url;
					} else {
						command = "xdg-open " + url;
					}
					
					Runtime.getRuntime().exec(command);
					browserOpened = true;
					System.out.println("✅ Браузер открыт через системную команду!");
				} catch (Exception ex) {
					System.out.println("❌ Не удалось открыть браузер автоматически");
				}
			}

			if (!browserOpened) {
				System.out.println("🌐 Откройте браузер вручную:");
				System.out.println("   " + url);
			}

			System.out.println("🎯 Приложение готово к работе!");
			System.out.println("📞 Поддержка: m.d.burobin@gmail.com | @bur_bone");

		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.out.println("❌ Ошибка при ожидании запуска приложения");
		} catch (Exception e) {
			System.out.println("❌ Неожиданная ошибка при открытии браузера");
			System.out.println("🌐 Откройте браузер вручную: http://localhost:8081");
		}
	}
}

