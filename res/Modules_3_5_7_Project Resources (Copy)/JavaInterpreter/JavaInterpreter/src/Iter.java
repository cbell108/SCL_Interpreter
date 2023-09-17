import java.util.ArrayList;

public class Iter
{
    private ArithmeticExpression expr1;
    private ArithmeticExpression expr2;
    private ArrayList<Integer> it = new ArrayList<Integer>();;

    public Iter(ArithmeticExpression expr1, ArithmeticExpression expr2)
    {
        if (expr1 == null || expr2 == null)
            throw new IllegalArgumentException ("null arithmetic expression argument");

        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public ArrayList<Integer> evaluate()
    {
        it.add(expr1.evaluate());
        it.add(expr2.evaluate());
        return it;
    }

}
