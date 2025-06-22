package org.example.codebase.enums;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum StatusEnum {
    DRAFT("DRAFT"), PENDING("PENDING"), DONE("DONE");

    private String title;

    private static final Map<String, StatusEnum> map = new HashMap<>();

    static {
        Arrays.stream(values())
                .forEach(m -> map.put(m.name(), m));
    }

    StatusEnum(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public static StatusEnum from(String code) {
        return map.getOrDefault(code, DRAFT);
    }
}
