package org.zreddit59.service;

import org.zreddit59.dto.RedditAboutData;

public interface RedditService {
    String getToken();

    RedditAboutData getAboutData( String topic, String token);

    Long getMessagesDataCount( String topic, String token );
}
