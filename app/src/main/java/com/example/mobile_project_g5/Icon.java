package com.example.mobile_project_g5;

public class Icon {
    private String icon_path;
    private String name;

    public Icon(String icon, String name) {
        this.icon_path = icon;
        this.name = name;
    }

    public String getIcon() {
        return icon_path;
    }

    public void setIcon(String icon) {
        this.icon_path = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
