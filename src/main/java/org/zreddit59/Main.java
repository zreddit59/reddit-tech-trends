package org.zreddit59;

import org.zreddit59.dto.RedditAboutData;
import org.zreddit59.service.LogService;
import org.zreddit59.service.impl.LogServiceImpl;
import org.zreddit59.service.impl.RedditServiceImpl;

public class Main {
    public static void main( String[] args ) {
        LogService logService = new LogServiceImpl();
        logService.log( "Hello world!", null );
        String token = new RedditServiceImpl().getToken();
        logService.log( token, null );
        RedditAboutData about = new RedditServiceImpl().getAboutData( "java", token );
        logService.log( about.getData()
                             .toString(), null );
        Long messagesCount = new RedditServiceImpl().getMessagesDataCount( "java", token );
        logService.log( "" + messagesCount, null );
    }


}