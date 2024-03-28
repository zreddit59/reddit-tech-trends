package org.zreddit59;

import org.zreddit59.dto.RedditAboutData;
import org.zreddit59.service.impl.RedditServiceImpl;

public class Main {
    public static void main( String[] args ) {
        System.out.println( "Hello world!" );
        String token=new RedditServiceImpl().getToken();
        System.out.println(token);
        RedditAboutData about = new RedditServiceImpl().getAboutData( "java", token );
        System.out.println(about.getData().toString());
        Long messagesCount = new RedditServiceImpl().getMessagesDataCount( "java", token );
        System.out.println(messagesCount);
    }



}