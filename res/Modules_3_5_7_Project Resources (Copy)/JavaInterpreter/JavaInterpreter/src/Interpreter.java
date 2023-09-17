import java.io.FileNotFoundException;


public class Interpreter {

    public static void main(String[] args) {
        try
        {
            Parser p = new Parser("test1.jl");
            Program prog = p.parse();
            prog.execute();
            // to see chart of tokens and lexemes
            LexicalAnalyzer lex = new LexicalAnalyzer("test1.jl");
            lex.printLex();
            //Memory.displayMemory(); // to see results of assignment statement
        }
        catch (ParserException e)
        {
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (LexicalException e)
        {
            e.printStackTrace();
        }
    }

}