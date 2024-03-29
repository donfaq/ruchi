package com.donfaq.ruchi.component.telegram.subscribers;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.GetMe;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Flow;

public class TgChatCommandSubscriber implements Flow.Subscriber<Message> {

    private final Logger log = LoggerFactory.getLogger(TgChatCommandSubscriber.class);
    private final TelegramBot bot;
    private final TextGeneratorCommand textGeneratorCommand;
    private final User botUser;
    private Flow.Subscription subscription;

    public TgChatCommandSubscriber(TelegramBot bot, TextGeneratorCommand textGeneratorCommand) {
        this.bot = bot;
        this.textGeneratorCommand = textGeneratorCommand;
        this.botUser = bot.execute(new GetMe()).user();
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        log.debug("TgChatCommandSubscriber subscribed");
        this.subscription.request(1);
    }

    @Override
    public void onNext(Message message) {
        try {
            boolean messageHasCommand = false;
            if (!Objects.isNull(message.entities())) {
                messageHasCommand = Arrays.stream(message.entities()).anyMatch(
                        entity -> Objects.equals(
                                entity.type(),
                                MessageEntity.Type.bot_command
                        )
                );
            }
            if (messageHasCommand) {
                onCommandMessage(message);
            }
        } finally {
            subscription.request(1);
        }
    }

    private void replyToMessage(Message message, String replyText) {
        bot.execute(
                new SendMessage(message.chat().id(), replyText)
                        .replyToMessageId(message.messageId())
                        .parseMode(ParseMode.Markdown)
        );
    }


    public void onCommandMessage(Message message) {
        MessageEntity commandEntity =
                Arrays.stream(message.entities())
                        .filter(entity -> entity.type().equals(MessageEntity.Type.bot_command))
                        .findFirst()
                        .orElseThrow();
        String command = message
                .text()
                .substring(
                        commandEntity.offset() + 1,
                        commandEntity.offset() + commandEntity.length()
                ).toLowerCase();
        if (command.contains("@")) {
            String[] parts = command.split("@");
            if (!botUser.username().equals(parts[1])) {
                log.info("other bot command");
                return;
            }
            command = parts[0];
        }
        String query = message
                .text()
                .substring(commandEntity.offset() + commandEntity.length())
                .trim();
        log.info("Received command '{}' query: '{}'", command, query);

        String defaultResponse = "Что-то пошло не так";
        String helpMessage =
                """
                        Вот список доступных команд:
                        - `/help` - выводит это сообщение
                        - `/ping` - pong
                        - `/speak [начало]` - текст на основе истории общения с кожаными мешками
                        - `/pron [начало]` - название порноролика
                        - `/kalik [начало]` - текст на языке народа калюмбаса
                        - `/woman [начало]` - типичное сообщение с женского форума
                        - `/gachi` - ваш астральный гачимучи-гороскоп
                        - `/spam` - кричащий рекламный заголовок
                        - `/vata` - белая и пушистая
                        """;
        switch (command) {
            case "ping" -> replyToMessage(message, "pong");
            case "speak" -> replyToMessage(message, textGeneratorCommand.speak(query).orElse(defaultResponse));
            case "pron" -> replyToMessage(message, textGeneratorCommand.pron(query).orElse(defaultResponse));
            case "gachi" -> replyToMessage(message, textGeneratorCommand.gachi(query).orElse(defaultResponse));
            case "kalik" -> replyToMessage(message, textGeneratorCommand.kalik(query).orElse(defaultResponse));
            case "woman" -> replyToMessage(message, textGeneratorCommand.woman(query).orElse(defaultResponse));
            case "spam" -> replyToMessage(message, textGeneratorCommand.spam(query).orElse(defaultResponse));
            case "vata" -> replyToMessage(message, textGeneratorCommand.vata(query).orElse(defaultResponse));
            case "help" -> replyToMessage(message, helpMessage);
            case "start" -> replyToMessage(message, "Привет! " + helpMessage);
            default -> replyToMessage(message,
                    """
                            Я такое не умею
                            Используй команду `/help` для просмотра всех команд
                            """
            );
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.error("Error occurred while processing telegram command message", throwable);
    }

    @Override
    public void onComplete() {
        log.debug("TgChatCommandSubscriber completed");
    }
}
