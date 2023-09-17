'''
Created on Jun 27, 2011

@author: dgayler
'''

from Employees import Employee
from InvalidSalaryExceptions import InvalidSalaryException

class SalariedEmployee(Employee):
    '''
    classdocs
    '''


    def __init__(self, name, ssn, birthday, yearlySalary):
        '''
        Constructor
        '''
        super (SalariedEmployee, self).__init__(name, ssn, birthday)
        if yearlySalary is None:
            raise ValueError ("invalid numeric argument")
        if yearlySalary < 0:
            raise InvalidSalaryException ("negative salary")
        self.yearlySalary = yearlySalary
        
    def weeklyPay (self):
        return self.yearlySalary / 52