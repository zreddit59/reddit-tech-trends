package org.zreddit59.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.zreddit59.dto.RedditAboutData;
import org.zreddit59.dto.RedditMessagesData;
import org.zreddit59.dto.RedditToken;
import org.zreddit59.service.LogService;
import org.zreddit59.service.RedditService;

import java.io.IOException;
import java.time.LocalDate;


public class RedditServiceImpl implements RedditService {
    LogService logService = new LogServiceImpl();
    public static final String AUTHORIZATION = "Authorization";
    public static final String ERROR         = "Error: ";

    @Override
    public String getToken() {

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request body with form parameters
        RequestBody formBody = new FormBody.Builder()
                .add( "grant_type", "password" )
                .add( "username", System.getenv( "Z_USERNAME" ) )
                .add( "password", System.getenv( "Z_PASSWORD" ) )
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url( "https://www.reddit.com/api/v1/access_token" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( AUTHORIZATION, System.getenv( "Z_AUTHORIZATION" ) )
                .post( formBody )
                .build();

        // Execute the request
        try ( Response response = client.newCall( request )
                                        .execute() ) {
            if ( response.isSuccessful() ) {
                String responseBody = response.body()
                                              .string();
                // Deserialize JSON response to RedditToken object
                RedditToken redditToken = mapper.readValue( responseBody, RedditToken.class );
                logService.log(  "Access Token: " + redditToken.getToken(),"info" );
                return redditToken.getToken();
            } else {
                logService.log(  ERROR + response.code() + " - " + response.message(),null );
            }
        } catch ( IOException e ) {
            throw new RedditWatcherException( e );
        }

        throw new RedditWatcherException( "Token fetch KO" );
    }


    @Override
    public RedditAboutData getAboutData( String topic, String token ) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request
        Request request = new Request.Builder()
                .url( "https://oauth.reddit.com/r/" + topic + "/about" )
                .addHeader( "User-Agent", "ChangeMeClient/0.1 by YourUsername" )
                .addHeader( AUTHORIZATION, "bearer " + token )
                .build();

        // Execute the request
        try ( Response response = client.newCall( request )
                                        .execute() ) {
            if ( response.isSuccessful() ) {
                String responseBody = response.body()
                                              .string();
                // Deserialize JSON response to AboutDataBean object
                RedditAboutData aboutData = mapper.readValue( responseBody, RedditAboutData.class );
                logService.log(  "Display Name: " + aboutData.getData()
                                                         .getDisplayName(),"info" );
                logService.log(  "Subscribers: " + aboutData.getData()
                                                        .getSubscribers(),"info" );
                return aboutData;
            } else {
                logService.log(  ERROR + response.code() + " - " + response.message(),null );
            }
        } catch ( IOException e ) {
            throw new RedditWatcherException( e );
        }


        throw new RedditWatcherException( "GET ABOUT " + topic + " KO" );
    }

    @Override
    public Long getMessagesDataCount( String topic, String token ) {
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request
        Request request = new Request.Builder()
                .url( "https://oauth.reddit.com/r/" + topic + "/new?limit=100" )
                .addHeader( "User-Agent", "ChangeMeClient/0.1 by YourUsername" )
                .addHeader( AUTHORIZATION, "bearer " + token )
                .build();

        // Execute the request
        try ( Response response = client.newCall( request )
                                        .execute() ) {
            if ( response.isSuccessful() ) {
                String responseBody = response.body()
                                              .string();
                // Deserialize JSON response to AboutDataBean object
                RedditMessagesData messagesData = mapper.readValue( responseBody, RedditMessagesData.class );
                return messagesData.getData()
                                   .getChildren()
                                   .stream()
                                   .map( RedditMessagesData.Child::getData )
                                   .filter( data -> data.getCreated()
                                                        .isAfter( LocalDate.now()
                                                                           .minusWeeks( 1 ) ) )
                                   .count();
            } else {
                logService.log(  ERROR + response.code() + " - " + response.message(),null );
            }
        } catch ( IOException e ) {
            throw new RedditWatcherException( e );
        }


        throw new RedditWatcherException( "GET MESSAGES of " + topic + " KO" );
    }

    class RedditWatcherException extends RuntimeException {
        public RedditWatcherException( Exception e ) {
            super( e );
        }

        public RedditWatcherException( String errorMessage ) {
            super( errorMessage );
        }

    }

}
