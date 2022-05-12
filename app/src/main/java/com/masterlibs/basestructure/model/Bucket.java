package com.masterlibs.basestructure.model;

import java.util.List;

public class Bucket {
    private String bucketPath;
    private String firstImageContainedPath;
    private List<String> paths;

    public Bucket(String name, String firstImageContainedPath) {
        this.bucketPath = name;
        this.firstImageContainedPath = firstImageContainedPath;
    }

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public int getSize() {
        return paths.size();
    }

    public String getBucketPath() {
        return bucketPath;
    }

    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }

    public String getFirstImageContainedPath() {
        return firstImageContainedPath;
    }

    public void setFirstImageContainedPath(String firstImageContainedPath) {
        this.firstImageContainedPath = firstImageContainedPath;
    }
}
