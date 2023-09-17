public class Token
{

    private int rowNumber;

    private int columnNumber;

    private String lexeme;

    private TokenType tokType;

    /**
     * @param rowNumber - must be positive
     * @param columnNumber - must be positive
     * @param lexeme - cannot be null nor empty
     * @param tokType - cannot be null
     * @throws IllegalArgumentException if any precondition is not satisfied
     */
    public Token(int rowNumber, int columnNumber, String lexeme,
                 TokenType tokType)
    {
        if (rowNumber <= 0)
            throw new IllegalArgumentException ("invalid row number argument");
        if (columnNumber <= 0)
            throw new IllegalArgumentException ("invalid column number argument");
        if (lexeme == null || lexeme.length() == 0)
            throw new IllegalArgumentException ("invalid lexeme argument");
        if (tokType == null)
            throw new IllegalArgumentException ("invalid TokenType argument");
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.lexeme = lexeme;
        this.tokType = tokType;
    }

    public int getRowNumber()
    {
        return rowNumber;
    }

    public int getColumnNumber()
    {
        return columnNumber;
    }

    public String getLexeme()
    {
        return lexeme;
    }

    public TokenType getTokType()
    {
        return tokType;
    }


}