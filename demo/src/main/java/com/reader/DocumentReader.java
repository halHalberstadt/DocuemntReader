package com.reader;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.NodeType;
import com.aspose.words.Paragraph;
// import com.aspose.words.Run;
import com.aspose.words.SaveFormat;

// import org.apache.pdfbox.*;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
// imports for regex
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentReader {
    License licWordToPdf;
    Document document;
    String path;
    String extension;
    ArrayList<String> queries = new ArrayList<>();

    DocumentReader() {
        // Create a license object to avoid limitations of the trial version
        // while reading the Word file
        try {
            this.licWordToPdf = new License();
            this.licWordToPdf.setLicense("Aspose.Words.lic");
        } catch (Exception e) {
            System.err.println("com.reader.DocumentReaderInitializationException: Error initializing DocumentReader. " +
                    "Nested Error: " + e); // make sure to print the error
        }
    }

    DocumentReader(String documentPath) {
        // Create a license object to avoid limitations of the trial version
        // while reading the Word file
        try {
            this.licWordToPdf = new License();
            this.licWordToPdf.setLicense("Aspose.Words.lic");
            this.setDocument(documentPath);
        } catch (Exception e) {
            System.err.println("com.reader.DocumentReaderInitializationException: Error initializing DocumentReader. " +
                    "Nested Error: " + e); // make sure to print the error
        }
    }

    /*
     * Set Document sets up the question list and the path to the document
     * we need to read.
     * NOTE: This function requires the path to the document
     */
    public void setDocument(String documentPath) {
        this.document = null; // reset document to ensure document isn't re-read.
        try {
            // set basic variables for document reading.
            this.path = documentPath;
            this.document = new Document(documentPath);
            this.extension = this.path.substring(path.lastIndexOf("."));
        } catch (Exception e) {
            this.document = null; // reset document to ensure document isn't re-read.
            System.err.println("com.reader.SetDocumentReaderException: Error setting up document text. " +
                    "Nested Error: " + e); // make sure to print the error
        }

    }

    /*
     * readDocument is meant to just read out what the document has without formatting.
     *
     */
    public void readDocument() {
        int line = 0;
        System.out.println("start reading \"" + path + "\"");
        try {
            for (Object obj : this.document.getChildNodes(NodeType.PARAGRAPH, true)) {
                Paragraph para = (Paragraph) obj;
                System.out.println("" + (++line) + " - " + para.toString(SaveFormat.TEXT));
            }
        } catch (Exception e) {
            System.err.println("com.reader.ReadDocumentReaderException: Error reading document text. " +
                    "Nested Error: " + e); // make sure to print the error
        }
        System.out.println("done reading \"" + path + "\"");
    }

    /* TODO finish comment
     * getDocumentText ...
     */
    public ArrayList<String> getDocumentText() {
        ArrayList<String> documentText = new ArrayList<>();
        int numberLinesRead = 0;
        System.out.println("start reading \"" + path + "\"");
        try {
            for (Object obj : this.document.getChildNodes(NodeType.BODY, true)) {
                Paragraph para = (Paragraph) obj;
                documentText.add(para.toString(SaveFormat.TEXT));
                numberLinesRead++;
            }

        } catch (Exception e) {
            System.err.println("com.reader.GetDocumentReaderTextException: Error getting document text. " +
                    "# lines read=" + numberLinesRead + "Nested Error: " + e); // make sure to print the error
        }
        System.out.println("done reading \"" + path + "\"");

        return documentText;
    }

    /*
     * findQuestions() will run through document items and store them into
     * a list of strings.
     */
    public void findQuestions() {
        /*
         * The way I find questions in the documents (word documents) is by
         */
        System.out.println("start finding questions @ \"" + path + "\"");
        try {
            //TODO need to make option for non-formatted questions which we need to get from the user.
            this.findFormattedQuestions();
            // this.findUnformattedQuestions();
        } catch (Exception e) {
            System.err.println("com.reader.DocumentReaderFindQuestionsException: Error finding questions from document text. " +
                    "Nested Error: " + e); // make sure to print the error
        }
        System.out.println("done finding questions @ \"" + path + "\"");

        int queryNumber = 0;
        for (String q : this.queries) {
            System.out.println("Query #" + (++queryNumber) + " found: \"" + q + "\"");
        }
    }

    /*
     * Useful Functions
     */

    /*
     * findFormattedQuestions goes through a loop finding any formatted
     * questions in the document, and saves them to the object not needing
     * a return type.
     * NOTE: this throws an error due to the nature of the objects used
     */
    private void findFormattedQuestions() throws Exception{
        for (Object obj : this.document.getChildNodes(NodeType.PARAGRAPH, true)) {
            Paragraph para = (Paragraph) obj;
            // This is to prevent error for calling .getListLevel(), etc. on null objects
            if (para.getListFormat().isListItem()) {
                // For the non-null objects we need to get how the "dots/letters" are formatted
                byte[] bites = para.getListFormat().getListLevel().getNumberFormat().getBytes(StandardCharsets.UTF_8);
                // The ordered list that we are looking for happen to only have a byte array size of 2
                // I am not sure why exactly, but this could break on larger lists
                if (bites.length == 2) {
                    // I need to trim up the line found in the formatted list.
                    String questionFound = para.toString(SaveFormat.TEXT).trim();
//                        System.out.println("" + line + " - " + questionFound);
                    this.queries.add(questionFound);
                }
            }
        }
    }

    /* TODO findUnformattedQuestions()
     * findUnformattedQuestions goes through a loop finding any questions that
     * are in plain text using regex to find them.
     * NOTE: this throws an error due to the nature of the objects used,
     * and that this is less accurate or not as assured to be accurate.
     */
    private void findUnformattedQuestions() throws Exception{

    }

    /*
     * Getters and Setters
     */

    public ArrayList<String> getQuestions() {
        return this.queries;
    }

    public String getExtension() {
        return this.extension;
    }
}
