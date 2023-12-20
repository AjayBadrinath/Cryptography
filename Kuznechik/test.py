import unittest as u
import Kuznechik1 as k1
'''
                                        Unit Test For GOST - R- 3412-2015 (Kuznechik)  


Author: AjayBadrinath
Date :  21-12-23


'''
class KuznechikTransformationTest(u.TestCase):
    k=0x8899aabbccddeeff0011223344556677fedcba98765432100123456789abcdef
    S_arr=[
        0xffeeddccbbaa99881122334455667700,
        0xb66cd8887d38e8d77765aeea0c9a7efc,
        0x559d8dd7bd06cbfe7e7b262523280d39,
        0x0c3322fed531e4630d80ef5c5a81c50b
        ]
    S_testcase=[
        0xb66cd8887d38e8d77765aeea0c9a7efc,
        0x559d8dd7bd06cbfe7e7b262523280d39,
        0x0c3322fed531e4630d80ef5c5a81c50b,
        0x23ae65633f842d29c5df529c13f5acda
        ]
    msg=0x1122334455667700ffeeddccbbaa9988
    R_arr=[
        0x00000000000000000000000000000100,
        0x94000000000000000000000000000001,
        0xa5940000000000000000000000000000,
        0x64a59400000000000000000000000000
        ]
    R_testcase=[
        0x94000000000000000000000000000001,
        0xa5940000000000000000000000000000,
        0x64a59400000000000000000000000000,
        0x0d64a594000000000000000000000000
        ]
    L_arr=[
        0x64a59400000000000000000000000000,
        0xd456584dd0e3e84cc3166e4b7fa2890d,
        0x79d26221b87b584cd42fbc4ffea5de9a,
        0x0e93691a0cfc60408b7b68f66b513c13
           ]
    L_testcase=[
        0xd456584dd0e3e84cc3166e4b7fa2890d,
        0x79d26221b87b584cd42fbc4ffea5de9a,
        0x0e93691a0cfc60408b7b68f66b513c13,
        0xe6a8094fee0aa204fd97bcb0b44b8580
        ]
    k_testcase=[
        0x8899aabbccddeeff0011223344556677,
        0xfedcba98765432100123456789abcdef,
        0xdb31485315694343228d6aef8cc78c44,
        0x3d4553d8e9cfec6815ebadc40a9ffd04,
        0x57646468c44a5e28d3e59246f429f1ac,
        0xbd079435165c6432b532e82834da581b,
        0x51e640757e8745de705727265a0098b1,
        0x5a7925017b9fdd3ed72a91a22286f984,
        0xbb44e25378c73123a5f32f73cdb6e517,
        0x72e9dd7416bcf45b755dbaa88e4a4043
            ]
    cipher=0x7f679d90bebc24305a468d42b9d4edcd
    a=k1.Kuznechik(msg,k)
    def test_STransformation(self):
        for i in range(len(self.S_arr)):
            self.assertEqual(self.a.S_Transformation(self.S_arr[i]),self.S_testcase[i])
    def test_check_S_Inverse(self):
        for i in range(len(self.S_arr)):
            self.assertEqual(self.a.S_Transformation(self.a.S_Inv_Transformation(self.S_testcase[i])),self.S_testcase[i])
    def test_R_Transformation(self):
        for i in range(len(self.R_arr)):
            self.assertEqual(self.a.R_Transformation(self.R_arr[i]),self.R_testcase[i])
    def test_L_transformation(self):
        for i in range(len(self.L_arr)):
            self.assertEqual(self.a.L_Transformation(self.L_arr[i]),self.L_testcase[i])
    def test_encryption(self):
        self.assertEqual(self.a.encrypt(),self.cipher)
    def test_decryption(self):
        self.assertEqual(self.a.decrypt(self.cipher),self.msg)
    def test_poly_mul(self):
        self.assertEqual(self.a.M(0b10101,0b1100),0b11111100)
    def test_poly_mod(self):
        self.assertEqual(self.a.Mod_Polynomial_Reduction(0b11111100,0b100010),0b00010010)

if __name__=='__main__':
   u.main()