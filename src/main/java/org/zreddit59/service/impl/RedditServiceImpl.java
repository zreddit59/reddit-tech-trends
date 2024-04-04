package org.zreddit59.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.zreddit59.dto.RedditAboutData;
import org.zreddit59.dto.RedditMessagesData;
import org.zreddit59.dto.RedditToken;
import org.zreddit59.service.RedditService;

import java.io.IOException;
import java.time.LocalDate;


public class RedditServiceImpl implements RedditService {

    @Override
    public String getToken() {

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request body with form parameters
        RequestBody formBody = new FormBody.Builder()
                .add( "grant_type", "password" )
                .add( "username", System.getenv("USERNAME") )
                .add( "password", System.getenv("PASSWORD") )
                .build();

        // Create the request
        Request request = new Request.Builder()
                .url( "https://www.reddit.com/api/v1/access_token" )
                .addHeader( "Content-Type", "application/x-www-form-urlencoded" )
                .addHeader( "Authorization", System.getenv("AUTHORIZATION") )
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
                System.out.println( "Access Token: " + redditToken.getToken() );
                return redditToken.getToken();
            } else {
                System.err.println( "Error: " + response.code() + " - " + response.message() );
            }
        } catch ( JsonMappingException e ) {
            throw new RuntimeException( e );
        } catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }

        throw new RuntimeException( "Token fetch KO" );
    }


    @Override
    public RedditAboutData getAboutData( String topic, String token ){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request
        Request request = new Request.Builder()
                .url("https://oauth.reddit.com/r/"+topic+"/about")
                .addHeader("User-Agent", "ChangeMeClient/0.1 by YourUsername")
                .addHeader("Authorization", "bearer "+token)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // Deserialize JSON response to AboutDataBean object
                RedditAboutData aboutData = mapper.readValue(responseBody, RedditAboutData.class);
                System.out.println("Display Name: " + aboutData.getData().getDisplayName());
                System.out.println("Subscribers: " + aboutData.getData().getSubscribers());
                return aboutData;
            } else {
                System.err.println("Error: " + response.code() + " - " + response.message());
            }
        } catch ( JsonMappingException e ) {
            throw new RuntimeException( e );
        } catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }


        throw new RuntimeException( "GET ABOUT "+topic+" KO" );
    }

    @Override
    public Long getMessagesDataCount( String topic, String token ){
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        // Create the request
        Request request = new Request.Builder()
                .url("https://oauth.reddit.com/r/"+topic+"/new?limit=100")
                .addHeader("User-Agent", "ChangeMeClient/0.1 by YourUsername")
                .addHeader("Authorization", "bearer "+token)
                .build();

        // Execute the request
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                // Deserialize JSON response to AboutDataBean object
                RedditMessagesData messagesData = mapper.readValue(responseBody, RedditMessagesData.class);
                long messagesCount = messagesData.getData()
                                                    .getChildren()
                                                    .stream()
                                                    .map( RedditMessagesData.Child::getData )
                                                    .filter( data -> data.getCreated()
                                                                         .isAfter( LocalDate.now()
                                                                                            .minusWeeks( 1 ) ) )
                                                    .count();
                return messagesCount;
            } else {
                System.err.println("Error: " + response.code() + " - " + response.message());
            }
        } catch ( JsonMappingException e ) {
            throw new RuntimeException( e );
        } catch ( JsonProcessingException e ) {
            throw new RuntimeException( e );
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }


        throw new RuntimeException( "GET MESSAGES of "+topic+" KO" );
    }

}
