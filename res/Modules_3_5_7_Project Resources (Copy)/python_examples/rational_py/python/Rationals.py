'''


@author: dgayler
'''

class Rational:
    '''
    implementation of rational numbers
    '''

    def __init__(self, a, b):
        '''
        '''
        if a is None:
            raise ValueError ('invalid integer argument')
        if b is None:
            raise ValueError ('invalid integer argument')
        self.num = a
        if b == 0:
            raise ZeroDivisionError
        self.denom = b
        if b < 0:
            self.denom = self.denom * (-1)
            self.num = self.num * (-1)
            
        self.reduce ()
   
    def gcd (self, x, y): 
        if x is None:
            raise ValueError ('invalid integer argument')
        if y is None:
            raise ValueError ('invalid integer argument')
        result = 0
        if y == 0:
            result = x
        else:
            result = self.gcd(y, x % y)
        return result
    
    def reduce (self):
        lcd = self.gcd(abs (self.num), abs (self.denom))
        self.num = self.num / lcd
        self.denom = self.denom / lcd
           
    def add (self, r):
        if r is None:
            raise ValueError ('invalid rational number argument')
        n = self.num * r.denom + self.denom * r.num
        d = self.denom * r.denom
        return Rational (n, d)


    def sub (self, r):
        if r is None:
            raise ValueError ('invalid rational number argument')
        n = self.num * r.denom - self.denom * r.num
        d = self.denom * r.denom
        return Rational (n, d)
    
    def mul (self, r):
        if r is None:
            raise ValueError ('invalid rational number argument')
        n = self.num *  r.num
        d = self.denom * r.denom
        return Rational (n, d)

    def div (self, r):
        if r is None:
            raise ValueError ('invalid rational number argument')
        n = self.num * r.denom
        d = self.denom * r.num
        return Rational (n, d)
