package study.buddy.api;

import org.junit.gen5.api.BeforeEach;
import org.junit.gen5.api.DisplayName;
import org.junit.gen5.api.Test;

import study.buddy.api.security.JWToken;

import static org.junit.jupiter.api.Assertions.fail;


 //TODO: yea idk wtf to do for this...yet...
public class JWTTokenTest {

    //120000
    JWToken tokenMaster = new JWToken();

    String jwtToken, userName;

    @BeforeEach
    public void InitializeValidateToken(){
        userName = "testuser";
        jwtToken = tokenMaster.createToken(userName);
    }
    @DisplayName("Token Tester Success")
    @Test
    public void testToken() {

        if(!(tokenMaster.validate(jwtToken))){
            fail();
        }

    }
}
