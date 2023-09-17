'''
Created on Jun 27, 2011

@author: dgayler
'''

from Employees import Employee 
from InvalidHourlyRateExceptions import InvalidHourlyRateException
from InvalidHoursExceptions import InvalidHoursException

class HourlyEmployee (Employee):
    
    def __init__ (self, name, ssn, birthday, rate, hours):
        super(HourlyEmployee, self).__init__(name, ssn, birthday)
        if rate is None:
            raise ValueError ("invalid numeric argument")
        if hours is None:
            raise ValueError ("invalid numeric argument")
        if rate < 0:
            raise InvalidHourlyRateException ("negative rate")
        self.rate = rate
        if hours < 0:
            raise InvalidHoursException ("negative hours")
        self.hours = hours
        
        
    def weeklyPay (self):
        return self.rate * self.hours