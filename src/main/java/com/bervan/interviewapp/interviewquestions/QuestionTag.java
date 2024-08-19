package com.bervan.interviewapp.interviewquestions;

public enum QuestionTag {
    JAVA_DB_TESTING("Java/DB/Testing"),
    FRAMEWORKS("Frameworks"),
    SECURITY("Security");

    private final String displayName;

    QuestionTag(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}