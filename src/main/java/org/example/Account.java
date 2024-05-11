package org.example;

import lombok.Getter;
import lombok.Setter;

public class Account {
    @Getter
    private String name;
    @Getter
    private int id_account;
    @Setter
    @Getter
    private Authorization authorization;
    public Account(){
        name = null;
        id_account = -1;
        authorization = Authorization.ANONYMOUS;
    }

    public void setAuthenticatedInfo(int id_account, String name, Authorization authorization){
        this.id_account = id_account;
        this.name = name;
        this.authorization = authorization;
    }

    public void resetInfo(){
        name = null;
        id_account = -1;
        authorization = Authorization.ANONYMOUS;
    }

}
