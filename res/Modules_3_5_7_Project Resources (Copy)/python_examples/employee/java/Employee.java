public abstract class Employee
{

	private String name;
	private String ssn;
	private Date birthday;
	
	public Employee (String name, String ssn, Date birthday)
	{
		this.name = name;
		this.ssn = ssn;
		this.birthday = birthday;
	}
	
	public Date getBirthday()
	{
		return birthday;
	}

	public String getName()
	{
		return name;
	}

	public String getSsn()
	{
		return ssn;
	}

	public abstract double weeklyPay ();
	
}
