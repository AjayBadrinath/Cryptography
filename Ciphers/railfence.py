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
    #k=list(s1)
    #k.pop(1)
    #k.pop(-1)
    #k.pop(2)
    #k.pop(-2)
    #print(k,s1)
    return s1
x1=railfence("wearediscoveredrunatonce",4)
i=0
#crappy bruteforcing you just need any cipher text to bruteforce 
while x1!="wearediscoveredrunatonce":
    x1=railfence(x1,4)
    print(x1)
    print(i)
    i+=1

