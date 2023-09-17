public class Id implements ArithmeticExpression
{

    private char ch;

    /**
     * @param ch - must be a valid identifier
     * @throws IllegalArgumentException if ch is not a valid identifier
     */
    public Id(char ch)
    {
        if (!LexicalAnalyzer.isValidIdentifier(ch))
            throw new IllegalArgumentException ("character is not a valid identifier");
        this.ch = ch;
    }

    @Override
    public int evaluate()
    {
        return Memory.fetch (ch);
    }

    public char getChar ()
    {
        return ch;
    }

}