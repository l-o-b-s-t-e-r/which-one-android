package com.project.decider.user;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Lobster on 30.04.16.
 */

@Entity
@Table(name = "user_info")
public class User implements Serializable{

    @Id
    private String name;

    private String password;

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
