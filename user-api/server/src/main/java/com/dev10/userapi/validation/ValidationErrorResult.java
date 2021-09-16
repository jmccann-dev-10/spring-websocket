package com.dev10.userapi.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResult {
    private final ArrayList<String> messages = new ArrayList<>();

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public void addMessage(String message, Object... args) {
        addMessage(String.format(message, args));
    }

    public void addMessages(List<String> messages) {
        this.messages.addAll(messages);
    }
}
