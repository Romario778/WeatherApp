package weather.APP;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


public class Bot extends TelegramLongPollingBot {
    private final WeatherApp weather = new WeatherApp();

    // Создание и подключение бота
    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return "TheBestWeather778Bot"; // username бота.
    }

    @Override
    public String getBotToken() {
        return "7698549608:AAFQLvKNxEZ5Zno6YhyCNGUfCV07Bc9OWcY"; // token бота.
    }

    // Обработка полученного сообщения.
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            String messageText = update.getMessage().getText(); // Получаем текст сообщения пользователя
            Long chatId = update.getMessage().getChatId(); // Получаем chatId пользователя
            switch (messageText) {
                case "/start" -> sendMessage(chatId, "Привет! Введите название города, чтобы узнать погоду.");
                default -> sendMessage(
                        chatId,
                        weather.getWeather(messageText));
            }
        }
    }

    // Отправка сообщения пользователю
    public void sendMessage(Long chatId, String messageText) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(messageText);
        sendMessage.setChatId(chatId);
        try {
            executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
