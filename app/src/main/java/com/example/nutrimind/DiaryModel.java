package com.example.nutrimind;

// This class represents a Diary Entry in your app.
public class DiaryModel {

    private String documentId;   // Holds the document ID from Firestore (unique identifier for each entry)
    private String feeling;      // Holds the feeling associated with the diary entry
    private long timestamp;      // Holds the timestamp when the diary entry was created (in milliseconds)

    // Firestore requires a no-argument constructor
    public DiaryModel() {}

    // Getter and setter methods for the documentId
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    // Getter and setter methods for the feeling
    public String getFeeling() {
        return feeling;
    }

    public void setFeeling(String feeling) {
        this.feeling = feeling;
    }

    // Getter and setter methods for the timestamp
    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
