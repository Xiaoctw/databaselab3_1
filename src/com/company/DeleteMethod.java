package com.company;

import org.omg.CORBA.INTERNAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeleteMethod {
    /**
     * 根据邮箱地址对邮箱进行删除操作,其他情况下不允许删除
     * @param connection 一个连接
     * @param emailAddr 一个邮箱地址
     */
    static void DeleteEmail(Connection connection, String emailAddr){
        String sql="delete from email where emailAddr=?";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,emailAddr);
            ps.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    /**
     * 删除某个人的
     * @param connection 一个连接
     * @param user 一个用户
     */
    static void DeleteExperience(Connection connection,String user){
        String sql="delete from experience where userName=?";
        try {
            PreparedStatement ps=connection.prepareStatement(sql);
            ps.setString(1,user);
            ps.executeUpdate();
        } catch (SQLException ignored) {
        }
    }

    /**
     * 把friend从user的好友表中删除
     * @param user 一个用户
     * @param friend 好友名称
     */
    static void DeleteFriend(Connection connection,String user,String friend){
        try {
            PreparedStatement ps=connection.prepareStatement("set FOREIGN_KEY_CHECKS =0");
            ps.executeUpdate();
            String sql1="delete from friend where userName=? and friendName=?";
            PreparedStatement ps1=connection.prepareStatement(sql1);
            ps1.setString(1,user);
            ps1.setString(2,friend);
            ps1.executeUpdate();
            String sql2="select groupID from friendGroup where userName=?;";
            PreparedStatement ps2=connection.prepareStatement(sql2);
            ps2.setString(1,user);
            ResultSet res = ps2.executeQuery();
            List<Integer> list=new ArrayList<>();
            while (res.next()){
                list.add(res.getInt(1));
            }
            String sql3="delete from FG where groupID=? and friendName=?";
            for (Integer integer : list) {
                PreparedStatement ps3=connection.prepareStatement(sql3);
                ps3.setInt(1,integer);
                ps3.setString(2,friend);
                ps3.executeUpdate();
            }
            PreparedStatement ps4=connection.prepareStatement("set FOREIGN_KEY_CHECKS =1");
            ps4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    static void DeleteLog(Connection connection,int log_id){
        PreparedStatement ps= null;
        try {
            //首先去除外键依赖
            ps = connection.prepareStatement("set FOREIGN_KEY_CHECKS =0");
            ps.executeUpdate();
            String sql="delete from Log where logID=?";
            PreparedStatement ps1=connection.prepareStatement(sql);
            ps1.setInt(1,log_id);
            ps1.executeUpdate();
            String sql1="delete from reply where replyedID=? or replyID=?";
            PreparedStatement ps2=connection.prepareStatement(sql1);
            ps2.setInt(1,log_id);
            ps2.setInt(2,log_id);
            ps2.executeUpdate();
            String sql2="delete from shareLog where logID=?";
            PreparedStatement ps3=connection.prepareStatement(sql2);
            ps3.setInt(1,log_id);
            ps3.executeUpdate();
            PreparedStatement ps4=connection.prepareStatement("set FOREIGN_KEY_CHECKS =1");
            ps4.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void DeleteShareLog(Connection connection,int log_id){
        String sql="delete from shareLog where logID=?";
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,log_id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static void DeleteUser(Connection connection,String user){
        List<Integer> groupIDs=getGroupIDs(connection,user);
        try {
            PreparedStatement ps1=connection.prepareStatement("set FOREIGN_KEY_CHECKS =0");
            ps1.execute();
            String sql2="delete from user where userName=?";
            PreparedStatement ps2=connection.prepareStatement(sql2);
            ps2.setString(1,user);
            ps2.executeUpdate();
            String sql3="delete from friend where userName=? or friendName=?";
            PreparedStatement ps3=connection.prepareStatement(sql3);
            ps3.setString(1,user);
            ps3.setString(2,user);
            ps3.executeUpdate();
            String sql4="delete from friendGroup where userName=?";
            PreparedStatement ps4=connection.prepareStatement(sql4);
            ps4.setString(1,user);
            ps4.executeUpdate();
            String sql5="delete from FG where friendName=?";
            PreparedStatement ps5=connection.prepareStatement(sql5);
            ps5.setString(1,user);
            String sql6="delete from FG where groupID=?";
            for (Integer id : groupIDs) {
                PreparedStatement ps6=connection.prepareStatement(sql6);
                ps6.setInt(1,id);
                ps6.executeUpdate();
            }
            List<Integer> logIds=getLogIDs(connection,user);
            for (Integer logId : logIds) {
                DeleteLog(connection,logId);
            }
            PreparedStatement ps7=connection.prepareStatement("set FOREIGN_KEY_CHECKS =1");
            ps7.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static List<Integer> getGroupIDs(Connection connection,String user){
        String sql="select groupID from friendGroup where userName=?";
        return getIntegers(connection, user, sql);
    }
    static List<Integer> getLogIDs(Connection connection,String user){
        String sql="select logID from Log where userName=?";
        return getIntegers(connection, user, sql);
    }

    private static List<Integer> getIntegers(Connection connection, String user, String sql) {
        List<Integer> list=new ArrayList<>();
        try {
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,user);
            ResultSet res = preparedStatement.executeQuery();
            while (res.next()){
                list.add(res.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
