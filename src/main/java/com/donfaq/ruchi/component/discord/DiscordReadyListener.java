package com.donfaq.ruchi.component.discord;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DiscordReadyListener extends ListenerAdapter {

    private final Logger log = LoggerFactory.getLogger(DiscordReadyListener.class);

    private void updateSlashCommands(Guild guild) {
        log.info("Updating bot slash commands list for guild '{}'", guild.getName());
        guild.updateCommands().addCommands(
                Commands.slash("ping", "pong"),
                Commands.slash("speak", "Фраза, сгенерированная из логов чата")
                        .addOption(OptionType.STRING, "начало", "Начало для фразы (может быть проигнорировано)")
                        .setNSFW(true),
                Commands.slash("gachi", "Gachimuchi-гороскоп").setNSFW(true),
                Commands.slash("kalik", "Фраза на языке калюмбаса").setNSFW(true),
                Commands.slash("pron", "Название порноролика").setNSFW(true),
                Commands.slash("woman", "Типичное сообщение на женском форуме").setNSFW(true),
                Commands.slash("spam", "Кричащий заголовок спам-рекламы").setNSFW(true),
                Commands.slash("vata", "Белая и пушистая").setNSFW(true)
        ).queue(
                (commands) -> log.info("successfully updated commands in guild '{}'", guild.getName()),
                (throwable) -> log.error("error during updating commands in guild '{}'", guild.getName(), throwable)
        );
    }

    @Override
    public void onGuildReady(@NotNull GuildReadyEvent event) {
        log.info("Guild ready event for guild: '{}'", event.getGuild().getName());
        updateSlashCommands(event.getGuild());

    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {
        log.info("Joined to guild: '{}'", event.getGuild().getName());
        updateSlashCommands(event.getGuild());
    }
}
