package ru.shaplov.models;

import java.util.Objects;

/**
 * @author shaplov
 * @since 17.10.2019
 */
public class Msg {
    private String name;
    private String text;

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Msg msg = (Msg) o;
        return Objects.equals(name, msg.name)
                && Objects.equals(text, msg.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, text);
    }
}
