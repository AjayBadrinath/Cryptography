def monoalphabetic_encrypt(password, key):
    alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    encrypted_text = ""
    
    for char in password:
        if char.isalpha():
            idx=alphabet.index(char.upper())
            if char.isupper():
                encrypted_char = key[idx].upper() 
            else:
                 encrypted_char=key[idx].lower()    
            encrypted_text += encrypted_char     
        else:
            encrypted_text += char
    return encrypted_text


password= input("Enter your password: ")
key = "XZNLWEBGJHQDYVTKFUOMPCIASR"
encrypted_text=monoalphabetic_encrypt(password, key)
print(encrypted_text)
