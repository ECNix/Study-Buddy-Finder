package study.buddy.api.user;

import java.io.Serializable;

public class UserID implements Serializable {
    private Long id;

    private String name;

    public UserID(){
        this.id = null;
        this.name = "";
    }

    public UserID(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
