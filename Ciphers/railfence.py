def railfence(st,key):
    l=2*key-2
    j=0
    s1=""
    m=0
    for i in range(key):
        m=i
        s1+=st[m]
        while m<len(st):
            print(m)
            try:
                #s1+=st[m]
                                if(l==0):
                                    m+=j
                                    s1+=st[m]
                                    continue
                                m+=l
                                s1+=st[m]
                                if(j==0):
                                    m+=l
                                    s1+=st[m]
                                    continue
                                m+=j
                                s1+=st[m]
            except Exception:
                break
        j+=2
        l-=2
    
    return s1
x1=railfence("theyareattackingfromthenorth",4)
i=0

print(x1)
'''
#crappy bruteforcing
while x1!="defendtheeastwall":
=======
#crappy bruteforcing you just need any cipher text to bruteforce 
while x1!="wearediscoveredrunatonce":
>>>>>>> d3068faa6351a971488fe9e7e85ff2463c7dc29e
    x1=railfence(x1,4)
    print(x1)
    print(i)
    i+=1
    '''

def decrypt(stt_cipher,key):
    l1=["" for i in range(len(stt_cipher))]
    # 0-len(x)  2-len-2 4 - len-k
    l=2*(key-1)
    j=0
    m=0
    i=0
    ol_val=0
    for w in range(key): 
        m=ol_val
        l1[m]=stt_cipher[i]
        i+=1
        while m<len(stt_cipher):
            #print(stt_cipher[i])
            try:
                #s1+=st[m]
                                if(l==0):
                                    m+=j
                                    l1[m]=stt_cipher[i]
                                    i+=1
                                    continue
                                if(j==0):
                                    m+=l
                                    l1[m]=stt_cipher[i]
                                    i+=1
                                    continue
                                m+=l
                                l1[m]=stt_cipher[i]
                                i+=1
                                
                                m+=j
                                l1[m]+=stt_cipher[i]
                                i+=1
                                
                                
            except Exception:
                break
        j+=2
        ol_val+=1
        l-=2
    s=""
    s=s.join(l1)
    print(s)

#decrypt("dnetleedheswlftaa",3)    
decrypt("tekoohracirmnreatanftetytghh",4)         

              




    
