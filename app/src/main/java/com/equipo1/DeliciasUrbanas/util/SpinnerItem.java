package com.equipo1.DeliciasUrbanas.util;

public class SpinnerItem {
    private String text;
    private int drawableResId;

    public SpinnerItem(String text, int drawableResId) {
        this.text = text;
        this.drawableResId = drawableResId;
    }

    public String getText() {
        return text;
    }

    public int getDrawableResId() {
        return drawableResId;
    }
}
