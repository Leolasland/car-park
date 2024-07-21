package ru.project.carpark.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import ru.project.carpark.enums.BotCommand;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ParserBotCommand {
    private static final String PREFIX_FOR_COMMAND = "/";
    private static final String SPACE = " ";
    private static final String EMPTY = "";

    public static Pair<String, String> parseCommand(String text) {
        if (text.contains(SPACE)) {
            int indexOfSpace = text.indexOf(SPACE);
            return Pair.of(text.substring(0, indexOfSpace), text.substring(indexOfSpace + 1));
        }
        return Pair.of(text, EMPTY);
    }

    public static BotCommand getCommandFromText(String text) {
        String upperCaseText = text.toUpperCase().trim().replace(PREFIX_FOR_COMMAND, EMPTY);
        BotCommand command = BotCommand.NONE;
        try {
            command = BotCommand.valueOf(upperCaseText);
        } catch (IllegalArgumentException e) {
            log.debug("Can't parse command: " + text);
        }
        return command;
    }

    public static boolean isCommand(String text) {
        return text.startsWith(PREFIX_FOR_COMMAND);
    }

    public static Integer parseEnterpriseShowCommand(String text) {
        return Integer.parseInt(text.trim());
    }
}
