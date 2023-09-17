public class HourlyEmployee extends Employee
{

	private double rate;
    private int hours;
    
    public HourlyEmployee (String name, String ssn, Date birthday, double rate, int hours) throws InvalidHourlyRateException, InvalidHoursException
    {
    	super (name, ssn, birthday);
    	if (rate < 0)
    		throw new InvalidHourlyRateException ("negative rate");
    	this.rate = rate;
    	if (hours < 0)
    		throw new InvalidHoursException ("negative hours");
    	this.hours = hours;
    }
    
	public double weeklyPay()
	{
		return rate * hours;
	}

}
