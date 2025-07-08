package com.sathwikhbhat.scm.helpers;

public enum MessageType {

    SUCCESS("green"),
    ERROR("red"),
    WARNING("yellow"),
    INFO("blue");

    private final String color;

    MessageType(String color) {
        this.color = color;
    }

    public String getBgClass() {
        return "bg-" + color + "-100";
    }

    public String getTextClass() {
        return "text-" + color + "-800";
    }

    public String getButtonClass() {
        return "text-" + color + "-500 focus:ring-" + color + "-400 hover:bg-" + color + "-200";
    }

    public String getDarkTextClass() {
        return "dark:text-" + color + "-400";
    }

}