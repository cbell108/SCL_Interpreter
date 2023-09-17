'''


@author: dgayler
'''

from Rationals import Rational

def PrintRational (r):
    if r is None:
        raise ValueError ('invalid rational number argument')
    print (str(r.num) + '/' + str(r.denom))


if __name__ == '__main__':
    try:
        r1 = Rational (1, 4)
        PrintRational (r1)
        r2 = Rational (2, -20)
        PrintRational (r2)
        r3 = Rational (33, -88)
        PrintRational (r3)
        PrintRational (r1.add(r2));
        PrintRational(r1.sub(r2))
        PrintRational (r1.mul(r2))
        PrintRational (r1.div(r2))
    except ZeroDivisionError:
        print ('division by 0')
    except ValueError as e:
        print (str(e))
    except Exception:
        print ('unknown error occurred - terminating')


