package org.zreddit59.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AboutDataBean {

    @JsonProperty("display_name")
    private String displayName;
    private int subscribers;

    // Getter and setter for displayName
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    // Getter and setter for subscribers
    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public String toString() {
        return "AboutDataBean{" +
                "displayName='" + displayName + '\'' +
                ", subscribers=" + subscribers +
                '}';
    }
}

