import java.beans.Expression;

public class AssignmentStatement implements Statement
{

    private ArithmeticExpression expr;

    private Id var;

    /**
     * @param var - cannot be null
     * @param expr - cannot be null
     * @throws IllegalArgumentException if either argument is null
     */
    public AssignmentStatement(Id var, ArithmeticExpression expr)
    {
        if (var == null)
            throw new IllegalArgumentException ("null Id argument");
        if (expr == null)
            throw new IllegalArgumentException ("null ArithmeticExpression argument");
        this.expr = expr;
        this.var = var;
    }

    public void execute()
    {
        Memory.store(var.getChar(), expr.evaluate());
    }

}