package com.donfaq.ruchi.config;

import com.donfaq.ruchi.component.discord.ChatToConsoleListener;
import com.donfaq.ruchi.component.discord.ReactToSlashCommand;
import com.donfaq.ruchi.config.properties.DiscordConfigProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DiscordClientConfig {
    private final DiscordConfigProperties discordConfig;
    private final ChatToConsoleListener chatToConsoleListener;
    private final ReactToSlashCommand reactToSlashCommand;

    @Bean
    public JDA configureDiscordClient() throws LoginException, InterruptedException {
        log.info("Initializing Discord client");
        JDA jda = JDABuilder
                .createDefault(this.discordConfig.getBotToken())
                .addEventListeners(
                        chatToConsoleListener,
                        reactToSlashCommand
                )
                .build()
                .awaitReady();

        Guild guild = jda.getGuildById(discordConfig.getGuildId());
        assert guild != null;
        log.info("Updating discord bot commands in specified guild: {}", guild);

        guild.updateCommands()
             .addCommands(
                     new CommandData("ping", "pong"),
                     new CommandData("speak", "Пишет в чат фразу, сгенерированную из логов чата")
                             .addOption(OptionType.STRING, "начало", "Начало для фразы (может быть проигнорировано)"),
                     new CommandData("gachi", "Gachimuchi-гороскоп"),
                     new CommandData("kalik", "Пишет в чат фразу на языке калюмбаса"),
                     new CommandData("pron", "Генерирует название порноролика"),
                     new CommandData("woman", "Генерирует типичное сообщение на женском форуме")
             )
             .queue();

        return jda;
    }
}
