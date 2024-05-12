package org.example;

import java.util.Date;

/**
 *Audit information about a certain command which has been entered.
 */
public class AuditEntry implements Listable{
    private int id_account;
    private String command;
    private Date date;
    private boolean success;

    public AuditEntry(int id_account, String command, Date date, boolean success) {
        this.id_account = id_account;
        this.command = command;
        this.date = date;
        this.success = success;
    }

    @Override
    public String toString() {
        return "id_account=" + id_account + ", command=" + command + ", date=" + date + ", success=" + success;
    }
}
