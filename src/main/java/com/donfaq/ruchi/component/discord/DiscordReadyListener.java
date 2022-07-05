package com.donfaq.ruchi.component.discord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DiscordReadyListener extends ListenerAdapter {

    private void updateSlashCommands(Guild guild) {
        log.info("Updating bot slash commands list for guild '{}'", guild.getName());
        guild.updateCommands()
             .addCommands(
                     new CommandData("ping", "pong"),
                     new CommandData("speak", "Фраза, сгенерированная из логов чата")
                             .addOption(OptionType.STRING, "начало", "Начало для фразы (может быть проигнорировано)"),
                     new CommandData("gachi", "Gachimuchi-гороскоп"),
                     new CommandData("kalik", "Фраза на языке калюмбаса"),
                     new CommandData("pron", "Название порноролика"),
                     new CommandData("woman", "Типичное сообщение на женском форуме"),
                     new CommandData("spam", "Кричащий заголовок спам-рекламы"),
                     new CommandData("vata", "Белая и пушистая")
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
