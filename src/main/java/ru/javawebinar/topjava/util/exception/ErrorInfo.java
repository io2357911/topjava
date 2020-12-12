package ru.javawebinar.topjava.util.exception;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorInfo {
    private final String url;
    private final ErrorType type;
    private final String detail;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ErrorInfo(@JsonProperty("url") CharSequence url, @JsonProperty("type") ErrorType type,
                     @JsonProperty("detail") String detail) {
        this.url = url.toString();
        this.type = type;
        this.detail = detail;
    }

    public String getUrl() {
        return url;
    }

    public ErrorType getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}