package com.socratesdiaz.personalnotes;

/**
 * Created by socratesdiaz on 10/20/16.
 */
public class NavigationDrawerItem {
    private int iconId;
    private String title;

    public NavigationDrawerItem(int iconId, String title) {
        this.iconId = iconId;
        this.title = title;
    }

    public int getIconId() {
        return iconId;
    }

    public String getTitle() {
        return title;
    }
}
