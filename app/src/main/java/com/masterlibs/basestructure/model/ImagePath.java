package com.masterlibs.basestructure.model;

import java.util.List;

public class ImagePath {
    private List<String> path;
    private int viewType;


    public ImagePath(List<String> path, int viewType) {
        this.path = path;
        this.viewType = viewType;
    }

    public List<String> getPath() {
        return path;
    }

    public void setPath(List<String> path) {
        this.path = path;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
