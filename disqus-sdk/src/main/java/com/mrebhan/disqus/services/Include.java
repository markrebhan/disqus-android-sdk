package com.mrebhan.disqus.services;

public enum Include {
    UNAPPROVED("unapproved"),
    APPROVED("approved"),
    SPAM("spam"),
    DELETED("deleted"),
    FLAGGED("flagged"),
    HIGHLIGHTED("highlighted");

    private String includeString;

    Include(String includeString) {
        this.includeString = includeString;
    }

    public String toString(Include include) {
        return include.includeString;
    }
}
