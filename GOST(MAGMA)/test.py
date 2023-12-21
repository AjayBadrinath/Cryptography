import unittest as u
import GOST as g1
class MAGMATest(u.TestCase):
    _test_t=[
        0xfdb97531,
        0x2a196f34,
        0xebd9f03a,
        0xb039bb3d]
    _test_expect_t=[
        0x2a196f34,
        0xebd9f03a,
        0xb039bb3d,
        0x68695433
    ]
    _test_g={
        0x87654321:0xfedcba98,
        0xfdcbc20c:0x87654321,
        0x7e791a4b:0xfdcbc20c,
        0xc76549ec:0x7e791a4b

    }
    _test_expect_g=[
        0xfdcbc20c,
        0x7e791a4b,
        0xc76549ec,
        0x9791c849
    ]

  
    _test_expect_key=[

        0xffeeddcc,0xbbaa9988,0x77665544,0x33221100,0xf0f1f2f3,0xf4f5f6f7,0xf8f9fafb,0xfcfdfeff,
        0xffeeddcc,0xbbaa9988,0x77665544,0x33221100,0xf0f1f2f3,0xf4f5f6f7,0xf8f9fafb,0xfcfdfeff,
        0xffeeddcc,0xbbaa9988,0x77665544,0x33221100,0xf0f1f2f3,0xf4f5f6f7,0xf8f9fafb,0xfcfdfeff,
        0xfcfdfeff, 0xf8f9fafb, 0xf4f5f6f7, 0xf0f1f2f3, 0x33221100, 0x77665544, 0xbbaa9988, 0xffeeddcc


    ]

    _Key_=0xffeeddccbbaa99887766554433221100f0f1f2f3f4f5f6f7f8f9fafbfcfdfeff
    _msg=0xfedcba9876543210

    shc=g1.GOST_MAGMA(_msg,_Key_)



    def test_t_function(self):
        for _ in range(len(self._test_t)):
            self.assertEqual(self.shc.Transform(self._test_t[_]),self._test_expect_t[_])
        
    def test_g_function(self):
        for _ in range(len(self._test_g)):
            self.assertEqual(self.shc.g_function(list(self._test_g.keys())[_],list(self._test_g.values())[_]),self._test_expect_g[_])

    def test_KeySchedule(self):
        l=self.shc.flatten(self.shc.KeySchedule())
        for _ in range(len(self._test_expect_key)):

            self.assertEqual(l[_],hex(self._test_expect_key[_]))
    

if __name__=="__main__":
    u.main()