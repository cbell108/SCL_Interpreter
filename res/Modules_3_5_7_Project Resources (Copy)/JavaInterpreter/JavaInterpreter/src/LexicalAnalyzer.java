import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class LexicalAnalyzer
{

    private List<Token> tokens;

    public LexicalAnalyzer(String fileName) throws FileNotFoundException, LexicalException
    {
        assert (fileName != null);
        tokens = new ArrayList<Token>();
        Scanner sourceCode = new Scanner (new File (fileName));
        int lineNumber = 0;
        while (sourceCode.hasNext())
        {
            String line = sourceCode.nextLine();
            processLine (line, lineNumber);
            lineNumber++;
        }
        tokens.add(new Token (lineNumber, 1, "EOS", TokenType.EOS_TOK));
        sourceCode.close();
    }

    private void processLine(String line, int lineNumber) throws LexicalException
    {
        assert (line != null && lineNumber >= 1);
        int index = 0;
        index = skipWhiteSpace (line, index);
        while (index < line.length())
        {
            String lexeme = getLexeme (line, lineNumber, index);
            TokenType tokType = getTokenType (lexeme, lineNumber, index);
            tokens.add(new Token (lineNumber + 1, index + 1, lexeme, tokType));
            index += lexeme.length();
            index = skipWhiteSpace (line, index);
        }
    }

    private TokenType getTokenType(String lexeme, int lineNumber, int columnNumber) throws LexicalException
    {
        assert (lexeme != null && lineNumber >= 1 && columnNumber >= 1);
        TokenType tokType = null;
        if (Character.isLetter(lexeme.charAt(0)))
        {
            if (lexeme.length() == 1)
                if (isValidIdentifier (lexeme.charAt(0)))
                    tokType = TokenType.ID_TOK;
                else
                    throw new LexicalException ("invalid lexeme at row number " +
                            (lineNumber + 1) + " and column " + (columnNumber + 1));

            else if (lexeme.equals("if"))
                tokType = TokenType.IF_TOK;
            else if (lexeme.equals("function"))
                tokType = TokenType.FUNCTION_TOK;
            else if (lexeme.equals("end"))
                tokType = TokenType.END_TOK;
            else if (lexeme.equals("else"))
                tokType = TokenType.ELSE_TOK;
            else if (lexeme.equals("for"))
                tokType = TokenType.FOR_TOK;
            else if (lexeme.equals("while"))
                tokType = TokenType.WHILE_TOK;
            else if (lexeme.equals("print"))
                tokType = TokenType.PRINT_TOK;
            else
                throw new LexicalException ("invalid lexeme at row number " +
                        (lineNumber + 1) + " and column " + (columnNumber + 1));
        }
        else if (Character.isDigit (lexeme.charAt(0)))
        {
            if (allDigits (lexeme))
                tokType = TokenType.CONST_TOK;
            else
                throw new LexicalException ("invalid lexeme at row number " +
                        (lineNumber + 1) + " and column " + (columnNumber + 1));
        }
        else if (lexeme.equals("+"))
            tokType = TokenType.ADD_TOK;
        else if (lexeme.equals("-"))
            tokType = TokenType.SUB_TOK;
        else if (lexeme.equals("*"))
            tokType = TokenType.MUL_TOK;
        else if (lexeme.equals("/"))
            tokType = TokenType.DIV_TOK;
        else if (lexeme.equals("\\"))
            tokType = TokenType.REV_DIV_TOK;
        else if (lexeme.equals("^"))
            tokType = TokenType.EXP_TOK;
        else if (lexeme.equals("%"))
            tokType = TokenType.MOD_TOK;
        else if (lexeme.equals("="))
            tokType = TokenType.ASSIGN_TOK;
        else if (lexeme.equals("("))
            tokType = TokenType.LEFT_PAREN_TOK;
        else if (lexeme.equals(")"))
            tokType = TokenType.RIGHT_PAREN_TOK;
        else if (lexeme.equals(">="))
            tokType = TokenType.GE_TOK;
        else if (lexeme.equals(">"))
            tokType = TokenType.GT_TOK;
        else if (lexeme.equals("<="))
            tokType = TokenType.LE_TOK;
        else if (lexeme.equals("<"))
            tokType = TokenType.LT_TOK;
        else if (lexeme.equals("=="))
            tokType = TokenType.EQ_TOK;
        else if (lexeme.equals("!="))
            tokType = TokenType.NE_TOK;
        else if (lexeme.equals(":"))
            tokType = TokenType.COL_TOK;
        else
            throw new LexicalException ("invalid lexeme at row number " +
                    (lineNumber + 1) + " and column " + (columnNumber + 1));
        return tokType;
    }

    private boolean allDigits(String s)
    {
        assert (s != null);
        int i = 0;
        while (i < s.length() && Character.isDigit(s.charAt(i)))
            i++;
        return i == s.length();
    }

    private String getLexeme(String line, int lineNumber, int index)
    {
        assert (line != null && lineNumber >= 1 && index >= 0);
        int i = index;
        while (i < line.length() && !Character.isWhitespace(line.charAt(i)))
            i++;
        return line.substring(index, i);
    }

    private int skipWhiteSpace(String line, int index)
    {
        assert (line != null && index >= 0);
        while (index < line.length() && Character.isWhitespace(line.charAt(index)))
            index++;
        return index;
    }

    public Token getNextToken() throws LexicalException
    {
        if (tokens.isEmpty())
            throw new LexicalException ("no more tokens");
        return tokens.remove(0);
    }

    public Token getLookaheadToken() throws LexicalException
    {
        if (tokens.isEmpty())
            throw new LexicalException ("no more tokens");
        return tokens.get(0);
    }

    public static boolean isValidIdentifier (char ch)
    {
        //return ch == 'A' || ch == 'B' || ch == 'C' || ch == 'D';
        return Character.isLetter(ch);
    }

    public void printLex(){ //print all tokens and lexemes
        for(int i = 0; i < tokens.size(); i++){
            TokenType tokType = tokens.get(i).getTokType();
            String lexeme = tokens.get(i).getLexeme();
            System.out.println("The next token is: " + tokType + " **** Next lexeme is: " + lexeme);
        }
    }
}