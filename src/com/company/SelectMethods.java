package com.company;

import org.junit.Before;
import org.junit.Test;

import java.sql.*;
import java.util.*;

public class SelectMethods {
    static final String JDBC_DRIVER="com.mysql.jdbc.Driver";
    static final String DB_URL="jdbc:mysql://localhost:3306/socialnetwork";
    String user="root";
    String password="98a-042-b-6";
    Connection connection=null;
    @Before
    public void init() throws SQLException, ClassNotFoundException {
        Class.forName(JDBC_DRIVER);
        connection= DriverManager.getConnection(DB_URL,user,password);
    }
    /**
     * 查询一个用户的所有邮箱地址
     * @param user
     * @return
     */
    static List<String> SelectEmail(Connection connection,String user){
        String sql="select emailAddr from user,email where user.userName=email.userName and user.userName=?;";
        List<String> list=new ArrayList<>();
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ResultSet res=ps.executeQuery();
            while (res.next()){
                list.add(res.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    @Test
    public void test1(){
        List<String> res=SelectEmail(connection,"li");
        for (String re : res) {
            System.out.println(re);
        }
    }

    /**
     * 查找一个用户的朋友和分组
     * @param connection 连接
     * @param user 用户
     * @return
     */
    static Map<String,String> SelectFriendGroup(Connection connection,String user){
        String sql="select friend.friendName,friendGroup.groupName from friend,friendGroup,FG where friend.friendName=" +
                "FG.friendName and FG.groupID=friendGroup.groupID and friendGroup.userName=?;";
        Map<String,String> map=new HashMap<>();
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ResultSet res=ps.executeQuery();
            while (res.next()){
                map.put(res.getString(1),res.getString(2));
            }
            String sql1="select friendName from friend where userName=?;";
            ps=connection.prepareStatement(sql1);
            ps.setString(1,user);
            ResultSet set=ps.executeQuery();
            while (set.next()){
                String val=set.getString(1);
                if(!map.containsKey(val)){
                    map.put(val,"没有分组");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    @Test
    public void test2(){
        Map<String,String > map=SelectFriendGroup(connection,"xiao");
        map.forEach((x,y)->{
            System.out.println(x+" "+y);
        });
    }

    /**
     * 查找一个用户发过的所有log
     * @param connection 一个连接
     * @param user 一个用户
     * @return 一个map
     */
    static Map<Integer,String> SelectLog(Connection connection, String user){
        String sql="select logID,content from Log where userName=?;";
        Map<Integer,String> map=new HashMap<>();
        return getIntegerStringMap(connection, user, map, sql);
    }
    @Test
    public void test3(){
        Map<Integer,String > map= SelectLog(connection,"xiao");
        map.forEach((x,y)->{
            System.out.println(x+" "+y);
        });
    }
    /*
        查找user回复过的log
     */
    static Map<Integer,String> SelectReplyedID(Connection connection,String user){
        Map<Integer,String> map=new HashMap<>();
        String sql="select l2.logID,l2.content from Log l1,Log l2,reply where l1.userName=? and l1.logID=replyID and " +
                "replyedID = l2.logID;";
        return getIntegerStringMap(connection, user, map, sql);
    }

    private static Map<Integer, String> getIntegerStringMap(Connection connection, String user, Map<Integer, String> map, String sql) {
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ResultSet set=ps.executeQuery();
            while (set.next()){
                map.put(set.getInt(1),set.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    @Test
    public void test4(){
        Map<Integer,String> map=SelectReplyedID(connection,"xiao");
        map.forEach((x,y)->{
            System.out.println(x+" "+y);
        });
    }

    /**
     * 查询每个分组中人数
     * @param connection
     * @return
     */
    static Map<Integer,Integer> SelectGroupNums(Connection connection,String user){
        String sql="select table1.groupID, table1.count from  friendGroup , (select groupID,count(*) as count from FG group by (groupID)) as table1 where friendGroup.userName=? and " +
                "friendGroup.groupID=table1.groupID";
        Map<Integer,Integer> map=new HashMap<>();
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ResultSet set=ps.executeQuery();
            while (set.next()) {
                map.put(set.getInt(1), set.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }
    @Test
    public void test5(){
        Map<Integer,Integer> map=SelectGroupNums(connection,"xiao");
        map.forEach((x,y)->{
            System.out.println(x+" "+y);
        });
    }
}
