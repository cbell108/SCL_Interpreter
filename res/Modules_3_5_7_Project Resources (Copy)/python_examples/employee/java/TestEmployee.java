public class TestEmployee
{

	public static void main(String[] args)
	{
		Employee e1 = null;
		Employee e2 = null;
		try
		{
			e1 = new SalariedEmployee("John Doe",  "012345678",	new Date(12, 12, 1976), 30000);
			e2 = new HourlyEmployee("Billy Bob Hogg", "000000000", 	new Date(4, 1, 1942), 6.15, 8);
		}
		catch (InvalidSalaryException e)
		{
			System.out.println (e.getMessage());
		}
		catch (InvalidHourlyRateException e)
		{
			System.out.println (e.getMessage());
		}
		catch (InvalidHoursException e)
		{
			System.out.println (e.getMessage());
		}
		printEmployee (e1);
		printEmployee (e2);
	}
	
	public static void printEmployee (Employee e)
	{
		System.out.println (e.getName() + ": "  + e.weeklyPay());
	}
}
