package com.company;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;


class InsertMethod {
    static void InsertIntoUser(Connection connection){
        Scanner in=new Scanner(System.in);
        System.out.println("姓名:");
        String name=in.next();
        in.nextLine();
        System.out.println("性别(f/m)");
        String sex=in.next();
        in.nextLine();
        System.out.println("通讯地址");
        String address=in.next();
        in.nextLine();
        System.out.println("用户密码:");
        String password=in.next();
        in.nextLine();
        System.out.println("出生日期(可不填):");
        String birthday=in.nextLine();
        String sql="insert into user values(?,?,?,?,?)";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,name);
            ps.setString(2,sex);
            ps.setString(3,address);
            ps.setString(4,password);
            if(birthday.equals("")){
                ps.setNull(5, Types.VARCHAR);
            }else {
                ps.setString(5,birthday);
            }
            ps.executeUpdate();
        } catch (MySQLIntegrityConstraintViolationException e) {
            System.out.println("当前用户名已经存在!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    static void InsertIntoEmial(Connection connection){
        String sql="insert into email values(?,?)";
        Scanner in=new Scanner(System.in);
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            System.out.println("用户名");
            String user=in.next();
            System.out.println("邮箱地址");
            String email=in.next();
            ps.setString(1,user);
            ps.setString(2,email);
            ps.executeUpdate();
        }catch (MySQLIntegrityConstraintViolationException e){
            String s=e.toString();
            if (s.contains("Cannot add or update a child row")){
                System.out.println("当前用户不存在!");
            }else {
                System.out.println("当前邮箱已经绑定用户!");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void InsertIntoExperience(Connection connection){
        Scanner in=new Scanner(System.in);
        int level;
        String sql="insert into experience values(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement statement=connection.prepareStatement(sql);
            System.out.println("用户名称");
            String s=in.next();
            in.nextLine();
            statement.setString(9,s);
            System.out.println("教育水平:(1为博士,2硕士,3为本科,4为高中及以下)");
            s=in.nextLine();
            if(s.equals("")){
                statement.setNull(1, Types.INTEGER);
            }else {
                level= Integer.parseInt(s);
                statement.setInt(1,level);
            }
            System.out.println("开始接受教育时间(可空)");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(2,s);
            }else {
                statement.setNull(2,Types.VARCHAR);
            }
            System.out.println("结束接受教育时间(可空)");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(3,s);
            }else {
                statement.setNull(3,Types.VARCHAR);
            }
            System.out.println("学位");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(4,s);
            }else {
                statement.setNull(4,Types.VARCHAR);
            }
            System.out.println("毕业学校");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(10,s);
            }else {
                statement.setNull(10,Types.VARCHAR);
            }
            System.out.println("工作单位");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(5,s);
            }else {
                statement.setNull(5,Types.VARCHAR);
            }
            System.out.println("开始工作时间(可空)");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(6,s);
            }else {
                statement.setNull(6,Types.VARCHAR);
            }
            System.out.println("结束工作时间(可空)");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(7,s);
            }else {
                statement.setNull(7,Types.VARCHAR);
            }
            System.out.println("工作职位");
            s=in.nextLine();
            if(!s.equals("")){
                statement.setString(8,s);
            }else {
                statement.setNull(8,Types.VARCHAR);
            }
            statement.executeUpdate();
        }catch (MySQLIntegrityConstraintViolationException e){
          //  e.printStackTrace();
            if (e.toString().contains("PRIMARY")){
                System.out.println("当前用户已经添加信息");
            }else {
                System.out.println("当前用户不存在");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 该函数的作用是给指定用户添加朋友信息
     */
    static void InsertIntoFriends(Connection connection,String user){
        Scanner in=new Scanner(System.in);
        System.out.println("请输入"+user+"好友的姓名:(空格分隔开)");
        String users=in.nextLine();
        String[] friends=users.split(" ");
        String sql="insert into friend values (?,?)";
        for (String friend : friends) {
            try {
                PreparedStatement ps=connection.prepareStatement(sql);
                ps.setString(1,friend);
                ps.setString(2,user);
                ps.executeUpdate();
            }catch (SQLException e){
                if (e.toString().contains("a foreign key constraint fails")){
                    System.out.println("出现错误,请检查当前用户是否已经添加!");
                } //  System.out.println("出现错误,当前好友关系已经添加过!");

            }
        }

    }

    /**
     * 给指定用户进行分组操作,添加分组
     * @param connection
     * @param user
     */
    static void InsertIntoGroups(Connection connection,String user,int GROUP_ID){
        Scanner in=new Scanner(System.in);
            System.out.println("请输入分组的名称:");
            String name = in.next();
            String sql = "insert into friendGroup values (?,?,?)";
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ps.setString(1, user);
                ps.setInt(2, GROUP_ID);
                ps.setString(3, name);
                ps.executeUpdate();
              //  GROUP_ID++;
            }catch (MySQLIntegrityConstraintViolationException e){
                if (e.toString().contains("a foreign key constraint fails")){
                    System.out.println("出现错误,请检查当前好友是否已经添加或者当前分组是否存在");
                }
            }
            catch (SQLException ignored) {
            }

    }
    static void InsertIntoFG(Connection connection,String friendName,int groupID){
        Scanner in=new Scanner(System.in);
        String sql="insert into FG values (?,?)";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,friendName);
            ps.setInt(2,groupID);
            ps.executeUpdate();
        } catch (MySQLIntegrityConstraintViolationException e) {
            if (e.toString().contains("a foreign key constraint fails")){
                System.out.println("出现错误,请检查当前好友是否已经添加或者当前分组是否存在");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 指定用户发Log,
     * @param connection 一个连接
     * @param user 一个用户
     */
    static void InsertIntoLog(Connection connection,String user,int logId){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        Scanner in=new Scanner(System.in);
        System.out.println("输入日志内容(空格表示不填)");
        String content=in.nextLine();
        String sql="insert into Log (userName, lastTime,content, logID) values (?,?,?,?);";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ps.setString(2,time);
            if(!content.equals("")) {
                ps.setString(3, content);
            }else {
                ps.setNull(3,Types.VARCHAR);
            }
            ps.setInt(4,logId);
            ps.executeUpdate();
        }catch(MySQLIntegrityConstraintViolationException e) {
            System.out.println(e);
            if (e.toString().contains("a foreign key constraint fails")){
                System.out.println("出现错误,请检查当前用户是否已经添加");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void InsertIntoReply(Connection connection,int replyId,int replyedId){
        String sql="insert into reply (replyedID, replyID) values (?,?);";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,replyedId);
            ps.setInt(2,replyId);
            ps.executeUpdate();
        }catch (MySQLIntegrityConstraintViolationException e){
            if (e.toString().contains("a foreign key constraint fails")){
                System.out.println("出现错误,请检查当前日志是否已经添加");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /*

     */
    static void InsertIntoSharedLog(Connection connection,int logID,String user){
        SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time=df.format(new Date());
        String content;
        Scanner in=new Scanner(System.in);
        System.out.println("请输入分享内容:");
        content=in.nextLine();
        String sql="insert into shareLog (logID, userName, shareTime, content, contentTime) values (?,?,?,?,?);";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setInt(1,logID);
            ps.setString(2,user);
            ps.setString(3,time);
            ps.setString(5,time);
            if(content.equals("")){
                ps.setNull(4,Types.VARCHAR);
            }else {
                ps.setString(4,content);
            }
            ps.executeUpdate();
        }catch (MySQLIntegrityConstraintViolationException e){
            //由于时间一定不一样,因此不会出现主键错误
            if (e.toString().contains("a foreign key constraint fails")){
                System.out.println("出现错误,请检查当前日志是否已经添加");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
