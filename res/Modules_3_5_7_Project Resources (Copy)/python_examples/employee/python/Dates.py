'''
Created on Jun 27, 2011

@author: dgayler
'''

from InvalidDateExceptions import InvalidDateException

class Date(object):
    '''
    classdocs
    '''


    def __init__(self, month, day, year):
        '''
        Constructor
        '''
        if month < 1 or month > 12:
            raise InvalidDateException ("invalid month number")
        self.month = month
        if day < 1 or day > 31:
            raise InvalidDateException ("invalid day number")
        self.day = day
        self.year = year