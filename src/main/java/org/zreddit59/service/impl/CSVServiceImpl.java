package org.zreddit59.service.impl;
import org.zreddit59.service.CSVService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CSVServiceImpl implements CSVService {

    private PrintWriter      pw;
    private String           fileName;
    private SimpleDateFormat dateFormat;

    public CSVServiceImpl(String fileName) {
        this.fileName = fileName;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            pw = new PrintWriter(new FileWriter(fileName, true));
            // Write the header if the file is newly created
            if (new java.io.File(fileName).length() == 0) {
                // Write the header if the file is empty
                pw.println("date,topic,subscribers,messages");
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void appendLine( String topic, int subscribers, String messages ) {
        String currentDate = dateFormat.format(new Date() );
        pw.println(currentDate + "," + topic + "," + subscribers + "," + messages);
    }

    @Override
    public void close() {
        pw.flush();
        pw.close();
    }

}

