import javax.swing.JOptionPane;

public class TestRational
{

	public static void main(String[] args)
	{
		try
		{
			Rational r1 = getRational();
			Rational r2 = getRational ();
			System.out.println (r1.add(r2));
			System.out.println (r1.sub(r2));
			System.out.println (r1.mul(r2));
			System.out.println (r1.div(r2));		
		}
		catch (DivisionByZeroException e)
		{
			System.out.println (e.getMessage());
		}
	}
	
	private static Rational getRational () throws DivisionByZeroException
	{
		String r = JOptionPane.showInputDialog ("enter a rational number in the form a / b");
		int i = r.indexOf('/');
		int n = Integer.parseInt(r.substring(0,i));
		int d = Integer.parseInt (r.substring (i+1));
		return new Rational (n, d);
	}
	
}
