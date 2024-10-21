package study.buddy.api.security;


//import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.stereotype.Service;
import study.buddy.api.user.User;

import java.io.*;

@Service
public class PassHash {
    Argon2PasswordEncoder encryptor;

    public PassHash () {
        encryptor = new Argon2PasswordEncoder(64,72,2,20*1024,20);
    }


    public void hashPass(User user, String raw){
        var hashedPass = encryptor.encode(raw);
        if(encryptor.matches(raw,hashedPass)){
            System.out.println("\nSetting " + user.getEmailAddress() + "'s password: " + user.getPassword());
            user.setPassword(hashedPass);
            System.out.println("\nDEBUG - raw: " + raw + "\nhashed: " + hashedPass + "\nUserpass: " + user.getPassword() + '\n');
        }else{
            System.out.println("I don't need this but if this is printing then something has gone horribly wrong");
        }
    }

    public Boolean validation(User user, String raw) {
        return encryptor.matches(raw, user.getPassword());
    }

}
