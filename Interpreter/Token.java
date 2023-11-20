/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 3rd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: Token.java
 * November 19, 2023
 */

import java.util.Optional;

public class Token {
    private String type;
    private String value;
    private int id;
    private Optional<Integer> lineNum;
    private static int idCounter = 101;

    public Token(String type, String value, int id) {
        this.type = type;
        this.value = value;
        this.id = id;
    }

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
        this.id = idCounter++;
    }

    public Token(int id, String value, String type, int lineNum) {
        this.lineNum = Optional.of(lineNum);
        this.id = id;
        this.value = value;
        this.type = type;
    }

    public Token(Token originalToken, int lineNum) {
        this.lineNum = Optional.of(lineNum);
        this.id = originalToken.getId();
        this.value = originalToken.getValue();
        this.type = originalToken.getTypeName();
    }

    public int getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public String getTypeName() {
        return type;
    }

    public int getLineNum() throws Exception {
        if (lineNum.isPresent()) {
            return lineNum.get();
        } else {
            throw new Exception("line num not initialized");
        }
    }

    @Override
    public String toString() {
        return "['" + type + "', " + id + ", '" + value.trim() + "']";
    }
}