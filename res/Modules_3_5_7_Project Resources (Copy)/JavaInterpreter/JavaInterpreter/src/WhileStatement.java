public class WhileStatement implements Statement
{
    private BooleanExpression expr;
    private Block blk;

    public WhileStatement(BooleanExpression expr, Block blk)
    {
        if (expr == null)
            throw new IllegalArgumentException ("null boolean expression argument");
        if (blk == null)
            throw new IllegalArgumentException ("null block argument");

        this.expr = expr;
        this.blk = blk;
    }

    @Override
    public void execute()
    {
        while (expr.evaluate())
            blk.execute();
    }

}
