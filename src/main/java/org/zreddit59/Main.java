package org.zreddit59;

import org.zreddit59.service.CSVService;
import org.zreddit59.service.LogService;
import org.zreddit59.service.impl.CSVServiceImpl;
import org.zreddit59.service.impl.LogServiceImpl;
import org.zreddit59.service.impl.RedditServiceImpl;

import java.util.List;

public class Main {

    private static final List<String> TOPICS = List.of( "java", "javascript", "Python", "csharp" );

    public static void main( String[] args ) {
        LogService logService = new LogServiceImpl();
        logService.log( "Starting Reddit Trends Watcher", null );
        CSVService csvService = new CSVServiceImpl( "topics.csv" );
        String token = new RedditServiceImpl().getToken();
        logService.log( token, null );
        for ( String topic : TOPICS ) {
            int subscribers = new RedditServiceImpl().getAboutData( topic, token )
                                                     .getData()
                                                     .getSubscribers();
            Long messagesCount = new RedditServiceImpl().getMessagesDataCount( topic, token );
            logService.log( topic + ", " + subscribers + ", " + messagesCount, null );
            csvService.appendLine( topic, subscribers, "" + messagesCount );
        }
        csvService.close();
    }

}