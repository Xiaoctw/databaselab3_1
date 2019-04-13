package com.company;


import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    private static final String DB_URL="jdbc:mysql://localhost:3306/socialnetwork";
    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
        String user=args[0];
        String password=args[1];
        Class.forName(JDBC_DRIVER);
        Connection connection=DriverManager.getConnection(DB_URL,user,password);
        Scanner in=new Scanner(System.in);
        label:
        while (true){
            System.out.println("输入操作:(1.插入,2.查询,3.删除,4.退出)");
            String line=in.next();
            switch (line) {
                case "4":
                    break label;
                case "2":
                    if (!delSelect(connection, in)) {
                        break label;
                    }
                    break;
                case "1":
                    if (!delInsert(connection, in)) {
                        break label;
                    }
                    break;
                case "3":
                    if (!delDelete(connection, in)) {
                        break label;
                    }
                    break;
                default:
                    break label;
            }
        }
    }
    private static boolean delSelect(Connection connection, Scanner in){
        System.out.println("输入查询内容:" +
                "\n" +
                "1.给定用户的所有邮箱\n" +
                "2.给定用户的朋友信息\n" +
                "3.给定用户的日志信息\n" +
                "4.给定用户回复过的回复信息\n" +
                "5.给定用户的朋友分组信息");
        int val=in.nextInt();
        if(val==1){
            System.out.println("输入用户姓名:");
            String name=in.next();
            List<String> list = SelectMethods.SelectEmail(connection, name);
            if (list.isEmpty()){
                System.out.println("该用户没有邮箱");
                return true;
            }
            System.out.println("邮箱如下:");
            for (String sql : list) {
                System.out.println(sql);
            }
        }else if(val==2){
            System.out.println("输入用户姓名:");
            String name=in.next();
            Map<String,String> map=SelectMethods.SelectFriendGroup(connection,name);
            if (map.isEmpty()){
                System.out.println("该用户没有添加朋友");
            }
            for (Map.Entry<String,String> entry:map.entrySet()){
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }else if(val==3){
            System.out.println("输入用户姓名:");
            String name=in.next();
            Map<Integer,String> map=SelectMethods.SelectLog(connection,name);
            if (map.size()==0){
                System.out.println("无日志");
                return true;
            }
            System.out.println("日志ID 日志内容");
            for (Map.Entry<Integer,String> entry:map.entrySet()){
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }else if(val==4){
            System.out.println("输入用户姓名:");
            String name=in.next();
            Map<Integer,String> map=SelectMethods.SelectReplyedID(connection,name);
            System.out.println("日志ID 日志内容");
            if (map.size()==0){
                System.out.println("无日志");
                return true;
            }
            for (Map.Entry<Integer,String> entry:map.entrySet()){
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }else if(val==5){
            System.out.println("输入用户姓名:");
            String name=in.next();
            Map<Integer,Integer> map=SelectMethods.SelectGroupNums(connection,name);
            if (map.size()==0){
                System.out.println("无分组");
                return true;
            }
            System.out.println("分组编号 人数");
            for (Map.Entry<Integer,Integer> entry:map.entrySet()){
                System.out.println(entry.getKey()+" "+entry.getValue());
            }
        }else {
            return false;
        }
        return true;
    }
    private static boolean delInsert(Connection connection, Scanner in) throws FileNotFoundException {
        System.out.println("输入插入内容:\n" +
                "1.插入新用户\n" +
                "2.插入电子邮箱\n" +
                "3.插入给定用户个人经历\n" +
                "4.指定用户添加朋友\n" +
                "5.指定用户添加分组\n" +
                "6.指定用户插入日志\n" +
                "7.指定用户发布分享日志\n" +
                "8.插入回复关系\n" +
                "9.给指定用户朋友分组");
        int val=in.nextInt();
        switch (val){
            case 1:
                InsertMethod.InsertIntoUser(connection);
                return true;
            case 2:
                InsertMethod.InsertIntoEmial(connection);
                return true;
            case 3:
                InsertMethod.InsertIntoExperience(connection);
                return true;
            case 4:
                System.out.println("输入用户名称");
                String user=in.next();
                InsertMethod.InsertIntoFriends(connection,user);
                return true;
            case 5:
                System.out.println("输入姓名");
                String name=in.next();
                int v;
                Scanner scanner=new Scanner(new File("/home/xiao/IdeaProjects/数据库实验3_1/next_groupID"));
                v=scanner.nextInt();
                scanner.close();
                InsertMethod.InsertIntoGroups(connection,name,v);
                v++;
                PrintStream printStream= new PrintStream(new File("/home/xiao/IdeaProjects/数据库实验3_1/next_groupID"));
                printStream.print(v);
                return true;
            case 6:
                System.out.println("输入姓名");
                String s=in.next();
                int v1;
                Scanner scanner1=new Scanner(new File("/home/xiao/IdeaProjects/数据库实验3_1/next_logID"));
                v1=scanner1.nextInt();
                scanner1.close();
                InsertMethod.InsertIntoLog(connection,s,v1);
                v1++;
                PrintStream printStream1= new PrintStream(new File("/home/xiao/IdeaProjects/数据库实验3_1/next_logID"));
                printStream1.print(v1);
                return true;
            case 7:
                System.out.println("输入姓名和被分享日志ID(空格分开)");
                String s1=in.next();
                int v2=in.nextInt();
                InsertMethod.InsertIntoSharedLog(connection,v2,s1);
                return true;
            case 8:
                System.out.println("输入回复的日志和被回复的日志(空格分开)");
                int reply=in.nextInt();
                int replyed=in.nextInt();
                InsertMethod.InsertIntoReply(connection,replyed,reply);
                return true;
            case 9:
                System.out.println("输入用户名称和其有人名称(空格隔开)");
                String use,friend;
                use=in.next();
                friend=in.next();
                System.out.println("输入分组名称");
                String groupname=in.next();
                int id=0;
                try {
                    PreparedStatement p=connection.prepareStatement("select groupID from friendGroup where userName=? and groupName=?;");
                  //  friend=in.next();
                    p.setString(1,use);
                    p.setString(2,groupname);
                    ResultSet set=p.executeQuery();
                    while (set.next()){
                        id=set.getInt(1);
                    }
                }catch (MySQLIntegrityConstraintViolationException e) {
                    System.out.println("出现错误,请检查当前用户或者分组是否存在");
                    return true;
                }catch (SQLException e) {
                    e.printStackTrace();
                }
                InsertMethod.InsertIntoFG(connection,friend,id);
                return true;
            default:return false;
        }
    }
    private static boolean delDelete(Connection connection, Scanner in){
        System.out.println("输入删除内容:\n" +
                "1.删除一个用户\n" +
                "2.删除一个朋友\n"+
                "3.删除一个日志"
                );
         int val=in.nextInt();
         if(val==1){
             System.out.println("输入用户姓名");
             String name=in.next();
             DeleteMethod.DeleteUser(connection,name);
         }else if(val==2){
             System.out.println("输入用户姓名和朋友姓名(空格分开)");
             String s1,s2;
             s1=in.next();
             s2=in.next();
             DeleteMethod.DeleteFriend(connection,s1,s2);
         }else if(val==3){
             System.out.println("输入日志ID");
             int id=in.nextInt();
             DeleteMethod.DeleteLog(connection,id);
         } else {
             return false;
         }
         return true;
    }
}
