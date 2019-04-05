package com.company;


import java.sql.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class Main {
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/socialnetwork";
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
	// write your code here
        String user=args[0];
        String password=args[1];
        Class.forName(JDBC_DRIVER);
        Connection connection=DriverManager.getConnection(DB_URL,user,password);
      //  DeleteMethod.DeleteEmail(connection,"12345678");
       // DeleteMethod.DeleteExperience(connection,"han");
      //  DeleteMethod.DeleteFriend(connection,"xiao","wang");
     //   DeleteMethod.DeleteLog(connection,5);
     //   DeleteMethod.DeleteShareLog(connection,1);
        DeleteMethod.DeleteUser(connection,"del");
    }

}
