'''
Created on Jun 27, 2011

@author: dgayler
'''

import abc

class Employee(object):
    '''
    classdocs
    '''
    

    def __init__(self, name, ssn, birthday):
        '''
        Constructor
        '''
        if name is None:
            raise ValueError ("invalid name argument")
        if ssn is None:
            raise ValueError ("invalid ssn argument")
        if birthday is None:
            raise ValueError ("invalid date argument")
        self.name = name
        self.ssn = ssn
        self.birthday = birthday
        
    @abc.abstractmethod
    def weeklyPay (self):
        return   
    