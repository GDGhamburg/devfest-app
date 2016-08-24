package de.devfest.model;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

public class SocialLink {

    public final String name;
    public final String link;
    public final int icon;
    public final int color;

    public SocialLink(String name, String link, @DrawableRes int icon, @ColorRes int color) {
        this.name = name;
        this.link = link;
        this.icon = icon;
        this.color = color;
    }
}
