package com.mrebhan.disqus.endpoints;

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
