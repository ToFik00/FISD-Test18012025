package org.piva.fisdtest18012025.chat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Command {
    LIST("/list"),
    WEATHER("/weather"),
    EXCHANGE("/exchange"),
    QUIT("/quit");

    private final String value;

    Command(String value) {
        this.value = value;
    }

    private static final Map<String, Command> map;

    static {
        map = Arrays.stream(Command.values())
                .collect(Collectors.toMap(Command::getValue, Function.identity()));
    }

    public String getValue() {
        return value;
    }

    public static Command getByValue(String value) {
        return map.get(value);
    }

    public static List<String> getAllCommandsValues() {
        return new ArrayList<>(map.keySet());
    }
}
