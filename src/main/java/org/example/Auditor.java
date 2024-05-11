package org.example;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class Auditor {
    static public void audit(String command, Account account, Connection connection, boolean success) throws SQLException {
        LocalDate currentDate = LocalDate.now();
        PreparedStatement statement = connection.prepareStatement("insert into Audit(id_account, command, date, success) values(?, ?, ?, ?)");
        statement.setInt(1, account.getId_account());
        statement.setString(2, command);
        statement.setDate(3, Date.valueOf(currentDate));
        statement.setBoolean(4, success);
        statement.executeQuery();
    }
}
