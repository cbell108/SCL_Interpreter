'''
Created on Jun 27, 2011

@author: dgayler
'''

import datetime
from SalariedEmployees import SalariedEmployee
from HourlyEmployees import HourlyEmployee
from InvalidSalaryExceptions import InvalidSalaryException
from InvalidHourlyRateExceptions import InvalidHourlyRateException
from InvalidHoursExceptions import InvalidHoursException

def printEmployee (e):
    if e is None:
        raise ValueError ("invalid employee argument")
    print (e.name + ": " + repr(e.weeklyPay()))
        
if __name__ == '__main__':
    try:
        e1 = SalariedEmployee("John Doe",  "012345678", datetime.date(1976, 12, 12), 30000)
        e2 = HourlyEmployee("Billy Bob Hogg", "000000000", datetime.date(1942, 4, 1), 6.15, 8)
        printEmployee (e1)
        printEmployee (e2)
    except InvalidSalaryException as e:
        print (e.message)
    except InvalidHourlyRateException as e:
        print (e.message)
    except InvalidHoursException as e:
        print (e.message)
    except ValueError as  e:
        print (e.message)
        
