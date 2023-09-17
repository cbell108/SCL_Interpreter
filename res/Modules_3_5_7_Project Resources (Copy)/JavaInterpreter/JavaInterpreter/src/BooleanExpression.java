public class BooleanExpression {

    private ArithmeticExpression expr1, expr2;
    private RelativeOperator op;

    public BooleanExpression(RelativeOperator op, ArithmeticExpression expr1, ArithmeticExpression expr2)
    {
        if (op == null)
            throw new IllegalArgumentException ("null relative operator argument");
        if (expr1 == null || expr2 == null)
            throw new IllegalArgumentException ("null arithmetic expression argument");

        this.op = op;
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public boolean evaluate()
    {
        boolean value = false;

        if (op == RelativeOperator.LE_OP)
            value = expr1.evaluate() <= expr2.evaluate();
        else if (op == RelativeOperator.LT_OP)
            value = expr1.evaluate() < expr2.evaluate();
        else if (op == RelativeOperator.GE_OP)
            value = expr1.evaluate() >= expr2.evaluate();
        else if (op == RelativeOperator.GT_OP)
            value = expr1.evaluate() > expr2.evaluate();
        else if (op == RelativeOperator.EQ_OP)
            value = expr1.evaluate() == expr2.evaluate();
        else if (op == RelativeOperator.NE_OP)
            value = expr1.evaluate() != expr2.evaluate();

        return value;
    }
}
