package com.donfaq.ruchi.component.telegram;

import com.donfaq.ruchi.component.commands.TextGeneratorCommand;
import com.donfaq.ruchi.component.telegram.subscribers.TgChatCommandSubscriber;
import com.donfaq.ruchi.component.telegram.subscribers.TgChatToConsoleSubscriber;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.SubmissionPublisher;

public class TgUpdatesListener implements UpdatesListener {
    private final TelegramBot bot;
    private final TextGeneratorCommand textGeneratorCommand;

    public TgUpdatesListener(TelegramBot bot, TextGeneratorCommand textGeneratorCommand) {
        this.bot = bot;
        this.textGeneratorCommand = textGeneratorCommand;
    }

    @Override
    public int process(List<Update> updates) {
        SubmissionPublisher<Message> publisher = new SubmissionPublisher<>();
        publisher.subscribe(new TgChatToConsoleSubscriber());
        publisher.subscribe(new TgChatCommandSubscriber(bot, textGeneratorCommand));
        updates.forEach(
                update -> {
                    if (!Objects.isNull(update.message())) {
                        if (!Objects.isNull(update.message().text())) {
                            publisher.submit(update.message());
                        }
                    }
                    if (!Objects.isNull(update.channelPost())) {
                        if (!Objects.isNull(update.channelPost().text())) {
                            publisher.submit(update.channelPost());
                        }
                    }
                }
        );

        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
