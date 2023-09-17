import java.util.ArrayList;
import java.util.List;

public class Block
{
    private List<Statement> stmts;

    public Block ()
    {

        stmts = new ArrayList<Statement>();
    }

    public void add(Statement stmt)
    {
        if (stmt == null)
            throw new IllegalArgumentException ("null statement argument");
        stmts.add(stmt);
    }

    public void execute()
    {
        for (int i = 0; i < stmts.size(); i++)
            stmts.get(i).execute();
    }
}
