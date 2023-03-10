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
    Document document;
    String path;
    String extension;
    ArrayList<String> queries = new ArrayList<String>();

    DocumentReader() {
        // Create a license object to avoid limitations of the trial version
        // while reading the Word file
        try {
            License licWordToPdf = new License();
            licWordToPdf.setLicense("Aspose.Words.lic");
        } catch (Exception e) {
            System.err.println("ERR - DocumentReader");
            System.err.println(e); // make sure to print the error
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
            System.out.println(this.extension);
        } catch (Exception e) {
            System.err.println("SetDocumentException: Nested Error:" + e); // make sure to print the error
        }

    }

    public void readDocument() {
        int line = 0;
        try {
                System.out.println("DOC - started reading -> " + path);
                for (Object obj : this.document.getChildNodes(NodeType.PARAGRAPH, true)) {
                    line++;
                    Paragraph para = (Paragraph) obj;
                    System.out.println("" + line + " - " + para.toString(SaveFormat.TEXT));
                }
                System.out.println("DOC - finished reading");
        } catch (Exception e) {
            System.err.println("ERR - readDocument");
            System.err.println(e); // make sure to print the error
        }
    }

    public ArrayList<String> getDocumentText() {
        ArrayList<String> documentText = new ArrayList<>();
        int numberLinesRead = 0;
        try {
            System.out.println("DOC - started reading");
            for (Object obj : this.document.getChildNodes(NodeType.BODY, true)) {
                Paragraph para = (Paragraph) obj;
                documentText.add(para.toString(SaveFormat.TEXT));
                numberLinesRead++;
            }

        } catch (Exception e) {
            System.err.println("ERR - getDocumentText. # lines read=" + numberLinesRead);
            System.err.println(e); // make sure to print the error
        }

        return documentText;
    }

    /*
     * findQuestions() will run through document items and store them into
     * a list of strings.
     */
    public void findQuestions() throws Exception {
        /*
         * The way I find questions in the documents (word documents) is by
         */
        int line = 0;
        for (Object obj : this.document.getChildNodes(NodeType.PARAGRAPH, true)) {
            line++;
            Paragraph para = (Paragraph) obj;
            if (para.getListFormat().isListItem()) {
                byte[] bites = para.getListFormat().getListLevel().getNumberFormat().getBytes(StandardCharsets.UTF_8);

                if (bites.length == 2) // 2 length is how long the bytes for numbered lists are
                    // There is no other marker that I have found as of yet
                    System.out.println("" + line + " - " + para.toString(SaveFormat.TEXT));
            }
        }

        for (String q : this.queries) {
            System.out.println(q);
        }
    }

    public ArrayList<String> getQuestions() {
        return this.queries;
    }

    public String getExtension(){
        return this.extension;
    }
}
