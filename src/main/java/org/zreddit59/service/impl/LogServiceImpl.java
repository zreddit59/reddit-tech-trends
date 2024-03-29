package org.zreddit59.service.impl;

import org.zreddit59.Main;
import org.zreddit59.service.LogService;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogServiceImpl implements LogService {
    private static final Logger      log = Logger.getLogger( Main.class.getName() );

    static {
        try {
            // Initialize the FileHandler once for the entire application lifecycle
            FileHandler fileHandler = new FileHandler("reddit.log", true);
            fileHandler.setFormatter( new SimpleFormatter() );
            log.addHandler( fileHandler );
            log.setLevel( Level.INFO );
        } catch ( IOException e ) {
            log.log( Level.SEVERE, "Error occurred", e );
        }
    }

    @Override
    public void log( String message, String level ) {
        // Check if level is null and assign a default level if necessary
        if (level == null) {
            level = "severe"; // Default level can be set to "info", "fine", or "severe"
        }
        // Log the message at the appropriate level
        switch ( level.toLowerCase() ) {
            case "info":
                log.info( message );
                break;
            case "fine":
                log.fine( message );
                break;
            default:
                log.severe( message );
                break;
        }
    }

}
