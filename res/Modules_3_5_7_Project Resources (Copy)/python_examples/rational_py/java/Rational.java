public class Rational
{

	private int num;

	private int denom;

	public Rational(int num, int denom) throws DivisionByZeroException
	{
		if (denom == 0)
			throw new DivisionByZeroException("attempt to create a rational number with a zero denominator");
		if (denom < 0)
		{
			denom *= -1;
			num *= -1;
		}
		this.denom = denom;
		this.num = num;
		reduce();
	}

	public Rational add(Rational r)
	{
		Rational result = new Rational (num * r.denom + r.num * r.denom, denom * r.denom);
		return result;
	}

	Rational sub(Rational r)
	{
		Rational result = new Rational (num * r.denom - r.num * denom, denom * r.denom);
		return result;
	}

	Rational mul(Rational r)
	{
		Rational result = new Rational (num * r.num, denom * r.denom);
		return result;
	}

	Rational div(Rational r)
	{
		if (r.num == 0)
			throw new DivisionByZeroException ("attempt to divide by 0");
		Rational result = new Rational (num * r.denom, denom * r.num);
		return result;
	}

	public int getNum()
	{
		return num;
	}

	public int getDenom()
	{
		return denom;
	}

	private int gcd(int x, int y)
	{
		int result;
		if (y == 0)
			result = x;
		else
			result = gcd(y, x % y);
		return result;
	}

	private void reduce()
	{
		int lcd = gcd(Math.abs(num), Math.abs(denom));
		num = num / lcd;
		denom = denom / lcd;
	}

	public String toString()
	{
		return num + "/" + denom;
	}

}
