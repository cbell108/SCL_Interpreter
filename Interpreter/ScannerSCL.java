/*
 * Kennesaw State University
 * College of Computer and Software Engineering
 * Department of Computer Science
 * CS 4308, Concepts of Programming Languages, Section W02
 * Project 3rd Deliverable
 * Connor Bell, Dylan Carder, Sebastian Utz, Kevin Vu
 * Program: SclScanner.java
 * November 19, 2023
*/
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.Exception;

// Scanner Class
public class ScannerSCL {
    /**
     * Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal
     */
    static List<List<Token>> TokenDictionary;
    /**
     * List of Token objects containing the final order of the tokens taken from the input file.
     */
    static List<Token> FinalTokenList;
    /**
     * Keeps track of which line is being scanned
     */
    static int LineNumber = 0;

    /**
     * Creates a Scanner object, getting initial tokens and creating an empty final list for tokenized output
     */
    public ScannerSCL() {}

    /**
     * Calls FilterScan, checks if it generated a list, creates a JsonWriterClass object that writes the JSON file.
     */
    public static List<Token> Scan(String Filepath) throws Exception {
        TokenDictionary = FillInitialTokens();
        FinalTokenList = new ArrayList<Token>();
        if (FilterScan(Filepath) == null) throw new Exception("file path is invalid.");//throw new Exception("FilterScan() returned an empty list");
        return FinalTokenList;
    }

    /**
     * Returns false if string Value is not added to FinalTokenList as a token
     */
    public static boolean CheckNormalToken(String Value) {
        // Identifies if current token is in any token category
        if (ContainsValue(Value)) {
            if (ContainsValue("keyword", Value)) {
                FinalTokenList.add(new Token(GetToken("keyword", Value), LineNumber));
            } else if (ContainsValue("identifier", Value)) {
                FinalTokenList.add(new Token(GetToken("identifier", Value), LineNumber));
            } else if (ContainsValue("operator", Value)) {
                FinalTokenList.add(new Token(GetToken("operator", Value), LineNumber));
            } else if (ContainsValue("special_symbol", Value)) {
                FinalTokenList.add(new Token(GetToken("special_symbol", Value), LineNumber));
            } else if (ContainsValue("type", Value)) {
                FinalTokenList.add(new Token(GetToken("type", Value), LineNumber));
            } else if (ContainsValue("literal", Value)) {
                FinalTokenList.add(new Token(GetToken("literal", Value), LineNumber));
            }
            return true;
        } else if (IsInt(Value) || IsFloat(Value)) { // a constant is a constant, not a general custom token
            FinalTokenList.add(new Token(new Token("constant", Value), LineNumber));
            return true;
        } else return false;
    }

    /**
     * Checks SCL operator and returns what it is
     */
    public static String CheckWhichOperator(String Operator) {
        if (Operator.contains("<=")) return "<=";
        if (Operator.contains(">=")) return ">=";
        if (Operator.contains("^")) return "^";
        if (Operator.contains("<")) return "<";
        if (Operator.contains(">")) return ">";
        if (Operator.contains("*")) return "*";
        if (Operator.contains("/")) return "/";
        if (Operator.contains("+")) return "+";
        if (Operator.contains("-")) return "-";
        if (Operator.contains("=")) return "=";
        if (Operator.contains("add")) return "add";
        return null;
    }

    /**
     * Adds the EndOfStatement token to the FinalTokenList
     */
    public static void AddEndOfStatementToken() {
        FinalTokenList.add(new Token(GetToken("end_of_statement", "EOS"), LineNumber));
    }

    /**
     * Adds the specified operator token using the Operator string
     */
    public static void AddOperator(String Operator) {
        switch (Operator) {
            case " +":
                FinalTokenList.add(new Token(GetToken("operator", 400), LineNumber));
                break;
            case " -":
                FinalTokenList.add(new Token(GetToken("operator", 401), LineNumber));
                break;
            case " *":
                FinalTokenList.add(new Token(GetToken("operator", 402), LineNumber));
                break;
            case " /":
                FinalTokenList.add(new Token(GetToken("operator", 403), LineNumber));
                break;
            case " ^":
                FinalTokenList.add(new Token(GetToken("operator", 404), LineNumber));
                break;
            case " >":
                FinalTokenList.add(new Token(GetToken("operator", 405), LineNumber));
                break;
            case " <":
                FinalTokenList.add(new Token(GetToken("operator", 406), LineNumber));
                break;
            case " =":
                FinalTokenList.add(new Token(GetToken("operator", 408), LineNumber));
                break;
            case "add":
                FinalTokenList.add(new Token(GetToken("operator", 409), LineNumber));
                break;
            case "<=":
                FinalTokenList.add(new Token(GetToken("operator", 410), LineNumber));
                break;
            case ">=":
                FinalTokenList.add(new Token(GetToken("operator", 411), LineNumber));
                break;
        }
        System.out.println(FinalTokenList.get(FinalTokenList.size() - 1));
    }

    /**
     * Filters File, returns a complete list of the list of the tokens
     */
    public static List<Token> FilterScan(String Filename) throws Exception{
    		LineNumber = 0;
    		try {
    			BufferedReader SCLFileReader = new BufferedReader(new FileReader(Filename));
    		boolean Comment = false;
            // Loop until end of line
            String line;
            while ((line = SCLFileReader.readLine()) != null) {
                // Read next line, add to LineNumber counter
                LineNumber++;
                // Filter out whitespace
                if (line.trim().isEmpty()) {
                    continue;
                }
                line = line.trim();
                // Filter out comments
                if (line.contains("/*")) Comment = true;
                if (line.contains("*/")) {
                    Comment = false;
                    continue;
                }
                if (Comment) continue;
                if (line.contains("//")) line = line.substring(0, line.indexOf("//"));
                // Break string into potential tokens
                String[] UnformattedSplitLine = line.split(" ");
                List<String> TempList = new ArrayList<String>();
                for (int i = 0; i < UnformattedSplitLine.length; i++) {
                    if (!UnformattedSplitLine[i].isEmpty()) TempList.add(UnformattedSplitLine[i]);
                }
                String[] SplitLine = TempList.toArray(new String[0]);
                // Validate potential tokens as tokens
                for (int i = 0; i < SplitLine.length; i++) {
                    // If string of the SplitLine is a normal token
                    if (CheckNormalToken(SplitLine[i])) {
                        continue;
                    }
                    // Filter out new literals
                    else if (SplitLine[i].contains("\"")) {
                        String Literal = SplitLine[i];
                        if ((Character.compare(SplitLine[i].charAt(0), '\"') == 0) && (Character.compare(SplitLine[i].charAt(SplitLine[i].length()-1), '\"') == 0)) {
                        //if (SplitLine[i].substring(0, 1).equals("\"") && SplitLine[i].substring(SplitLine[i].length() - 1, 1).equals("\"")) {
                            Token NewLiteral = new Token("literal", SplitLine[i]);
                            TokenDictionary.get(5).add(NewLiteral);
                            FinalTokenList.add(new Token(NewLiteral, LineNumber));
                        }
                        //else if ((!(Character.compare(SplitLine[i].charAt(0), '\"') == 0) && (Character.compare(SplitLine[i].charAt(SplitLine[i].length()-1), '\"') == 0)) || ((Character.compare(SplitLine[i].charAt(0), '\"') == 0) && !(Character.compare(SplitLine[i].charAt(SplitLine[i].length()-1), '\"') == 0))) throw new Exception("invalid syntax at line " + LineNumber + ".");
                        //else throw new Exception("invalid syntax at line " + LineNumber + ".");
                        for (int j = i + 1; j < SplitLine.length; j++) {
                            if (SplitLine[j].contains("\"")) {
                                boolean ContainsComma = false;
                                i = j;
                                if (SplitLine[j].charAt(SplitLine[j].length() - 1) == ',') {
                                    Literal += " " + SplitLine[j].substring(0, SplitLine[j].length() - 1);
                                    ContainsComma = true;
                                } else Literal += " " + SplitLine[j];
                                Token NewLiteral = new Token("literal", Literal);
                                TokenDictionary.get(5).add(NewLiteral);
                                FinalTokenList.add(new Token(NewLiteral, LineNumber));
                                if (ContainsComma) {
                                    FinalTokenList.add(new Token(GetToken("special_symbol", ","), LineNumber));
                                    ContainsComma = false;
                                }
                                break;
                            } else Literal += " " + SplitLine[j];
                        }
                    }
                    // Filter out operators
                    else if (SplitLine[i].contains("^") || SplitLine[i].contains("<") || SplitLine[i].contains(">") || SplitLine[i].contains("*") || SplitLine[i].contains("/") || SplitLine[i].contains("+") || SplitLine[i].contains("-") || SplitLine[i].contains("=")) {
                        if (SplitLine[i].length() > 1) {
                            int OpIndex = SplitLine[i].indexOf(CheckWhichOperator(SplitLine[i]));
                            String Before = SplitLine[i].substring(0, OpIndex);
                            if (!CheckNormalToken(Before)) {
                                Token NewToken = new Token("identifier", Before);
                                TokenDictionary.get(1).add(NewToken);
                                FinalTokenList.add(new Token(NewToken, LineNumber));
                            }
                            if (CheckWhichOperator(SplitLine[i]).length() == 1) AddOperator(CheckWhichOperator(SplitLine[i]));//CheckNormalToken(" " + CheckWhichOperator(SplitLine[i]));
                            else CheckNormalToken(CheckWhichOperator(SplitLine[i]));
                            String After = SplitLine[i].substring(OpIndex + 1);
                            if (!CheckNormalToken(After)) {
                                Token NewToken = new Token("identifier", After);
                                TokenDictionary.get(1).add(NewToken);
                                FinalTokenList.add(new Token(NewToken, LineNumber));
                            }
                        } else {
                            FinalTokenList.add(new Token(GetToken("operator", SplitLine[i]), LineNumber));
                        }
                    }
                    // Add identifier if not previously made
                    else {
                        Token NewToken = new Token("identifier", SplitLine[i]);
                        TokenDictionary.get(1).add(NewToken);
                        FinalTokenList.add(new Token(NewToken, LineNumber));
                    }
                }
                AddEndOfStatementToken();
            }
            // Prints to screen all newly created tokens form the SCL file
            // COMMENTS: part of original scanner implementation
            // COMMENTS: not needed in recent deliverables
            /*for (int i = 0; i < FinalTokenList.size(); i++)
            {
                System.out.println("New Token Created: " + FinalTokenList.get(i));
            }
            System.out.println("End of New Tokens\n");*/
            SCLFileReader.close();
            return FinalTokenList;
    		}
    		catch (Exception e) {
    			
    			return null;
    		}
    }

    /**
     * Returns true if string is a float.
     */
    public static boolean IsFloat(String num) {
        try {
            Float.parseFloat(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Returns true if string is an int.
     */
    public static boolean IsInt(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Gets the subdictionary list of the specified string Type; 
     * Types: keyword, operator, identifier, special_symbol, type, literal
     */
    private static List<Token> GetSubDict(String Type) {
        return switch (Type) {
            case "keyword" -> TokenDictionary.get(0);
            case "operator" -> TokenDictionary.get(1);
            case "identifier" -> TokenDictionary.get(2);
            case "special_symbol" -> TokenDictionary.get(3);
            case "type" -> TokenDictionary.get(4);
            case "literal" -> TokenDictionary.get(5);
            case "end_of_statement" -> TokenDictionary.get(6);
            case "constant" -> TokenDictionary.get(7);
            default -> throw new IllegalArgumentException("Looked for dictionary that does not exist");
        };
    }

    /**
     * Looks for token in TokenDictionary, if found returns true
     */
    private static boolean ContainsValue(String Value) {
        // needs to do switch statement with subdictionary selected by that
        for (List<Token> subDict : TokenDictionary) {
            for (Token token : subDict) {
                if (token.getValue().equals(Value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Looks for token in the token sublist, if found returns true;
     * Types: keyword, operator, identifier, special_symbol, unknown_symbol, literal
     */
    private static boolean ContainsValue(String Type, String Value) {
        // needs to do switch statement with subdictionary selected by that
        List<Token> subDict = GetSubDict(Type);
        for (Token token : subDict) {
            if (token.getValue().equals(Value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Looks for token in the token sublist, if not found returns null,
     * Types: keyword, operator, identifier, special_symbol, unknown_symbol, literal, end_of_statement
     */
    private static Token GetToken(String Type, String Value) {
        // needs to do switch statement with subdictionary selected by that
        List<Token> subDict = GetSubDict(Type);
        for (Token token : subDict) {
            if (token.getValue().equals(Value)) {
                return token;
            }
        }
        return null;
    }

    /**
     * Returns token with specified id, null if not found
     */
    private static Token GetToken(String Type, int ID) {
        // needs to do switch statement with subdictionary selected by that
        List<Token> subDict = GetSubDict(Type);
        for (Token token : subDict) {
            if (token.getId() == ID) {
                return token;
            }
        }
        return null;
    }

    /**
     * Returns a filled List dictionary of token objects.
     * Indexes: 0: keyword, 1 is identifier, 2: operator, 3: special_symbol, 4: type, 5: literal, 6: end_of_statement, 7: constant
     */
    private static List<List<Token>> FillInitialTokens() {
        List<List<Token>> TokenDict = new ArrayList<List<Token>>();
        List<Token> Keywords = new ArrayList<Token>();
        List<Token> Operators = new ArrayList<Token>();
        List<Token> SpecialSymbols = new ArrayList<Token>();
        List<Token> Identifiers = new ArrayList<Token>();
        List<Token> Types = new ArrayList<Token>();
        List<Token> Literals = new ArrayList<Token>();
        List<Token> EndOfStatements = new ArrayList<Token>();
        List<Token> Constants = new ArrayList<Token>();
        Keywords.add(new Token("keyword", "return", 0));
        Keywords.add(new Token("keyword", "print", 1));
        Keywords.add(new Token("keyword", "getline", 2));
        Keywords.add(new Token("keyword", "if", 3));
        Keywords.add(new Token("keyword", "elif", 4));
        Keywords.add(new Token("keyword", "else", 5));
        Operators.add(new Token("operator", "+", 400));
        Operators.add(new Token("operator", "-", 401));
        Operators.add(new Token("operator", "*", 402));
        Operators.add(new Token("operator", "/", 403));
        Operators.add(new Token("operator", ">", 404));
        Operators.add(new Token("operator", "<", 405));
        Operators.add(new Token("operator", "=", 406));
        Operators.add(new Token("operator", "<=", 407));
        Operators.add(new Token("operator", ">=", 408));
        SpecialSymbols.add(new Token("special_symbol", ",", 800));
        SpecialSymbols.add(new Token("special_symbol", ".", 801));
        Identifiers.add(new Token("identifier", "main", 100));
        Types.add(new Token("type", "int", 900));
        Types.add(new Token("type", "float", 901));
        Types.add(new Token("type", "char", 902));
        Types.add(new Token("type", "void", 903));
        Types.add(new Token("type", "bool", 904));
        EndOfStatements.add(new Token("end_of_statement", "EOS", 1000));
        TokenDict.add(Keywords);
        TokenDict.add(Identifiers);
        TokenDict.add(Operators);
        TokenDict.add(SpecialSymbols);
        TokenDict.add(Types);
        TokenDict.add(Literals);
        TokenDict.add(EndOfStatements);
        TokenDict.add(Constants);
        return TokenDict;
    }
}