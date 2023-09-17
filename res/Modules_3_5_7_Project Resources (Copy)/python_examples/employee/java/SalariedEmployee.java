public class SalariedEmployee extends Employee
{

	private double yearlySalary;
	
	public SalariedEmployee (String name, String ssn, Date birthday, double yearlySalary) throws InvalidSalaryException
	{
		super (name, ssn, birthday);
		if (yearlySalary < 0)
			throw new InvalidSalaryException ("negative yearly salary");
		this.yearlySalary = yearlySalary;
	}
	
	public double weeklyPay()
	{
		return yearlySalary / 52;
	}

}
