package com.reader;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the runner application for Plagarism Detection
 * NOTE: CURRENTLY DOES NOT ACCEPT PDF
 */
public class App {
    public static final String FILE_PATH = "C:\\Users\\smhal\\Documents\\Coding\\java\\docReader\\demo\\src\\main\\java\\com\\docs\\";

    public static void main(String[] args) {
        DocumentReader reader = new DocumentReader();
        ArrayList<String> fileLocations = findDocuments();

        for (String fileLocation : fileLocations) {
            System.out.println("Reading document @ " + fileLocation);
            reader.setDocument(fileLocation);
            // make sure I am reading the documents properly test
//             reader.readDocument();
            try {
                reader.findQuestions();
            } catch (Exception e){

                System.out.println("error finding questions error = " + e);
            }
            // now we read out questions found if any
//            ArrayList<String> questions = reader.getQuestions();
//            if (reader.getQuestions().size() == 0) {
//                System.out.println("No questions found");
//            } else {
//                System.out.println("Questions found");
//                int numberQuestion = 1;
//                for (String question : questions) {
//                    System.out.println("" + (numberQuestion++) + " - " + question);
//                }
//            }
        }

    }

    private static ArrayList<String> findDocuments() {
        // Folder path and initialization of returned list
        File folder = new File(FILE_PATH);
        ArrayList<String> listOfFileLocations = new ArrayList<String>();

        // grab all the files
        File[] listOfFiles = folder.listFiles();

        // grab all the files and dir at the location, then put them into the list
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                // System.out.println(listOfFiles[i].getName()); // check to make sure all files
                // are grabbed

                // Need to format the file location to get all exact paths
                listOfFileLocations.add(FILE_PATH + listOfFiles[i].getName());
            }
        }

        return listOfFileLocations;
    }
}
