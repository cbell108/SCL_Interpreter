public class Constant implements ArithmeticExpression
{

    private int value;

    public Constant(int value)
    {
        this.value = value;
    }

    @Override
    public int evaluate()
    {
        return value;
    }

}