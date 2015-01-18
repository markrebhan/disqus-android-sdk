package com.mrebhan.disqus.services;

public enum Related {
    FORUM("forum"),
    THREAD("thread");

    private String relatedString;

    Related(String relatedString) {
        this.relatedString = relatedString;
    }

    public String toString(Related related) {
        return related.relatedString;
    }
}
