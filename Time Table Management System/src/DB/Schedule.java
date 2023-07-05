package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ramith Gunawardana
 */
public class Schedule {
    String subjectid, department,floor,date,time = "";
    int number,capacity = 0;

    public Schedule(String subjectid, String department, String floor, int number, int capacity, String date, String time) {
        this.subjectid = subjectid;
        this.department = department;
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
        this.date = date;
        this.time = time;
    }
    
    public boolean isScheduleExsit() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = true;
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public boolean isScheduleExsit_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = true;
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_Backup_sqlite();
        }
        
        return retVal;
    }
    
    public boolean addSchedule() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isScheduleExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO schedule (subjectid, department, floor, number, capacity, date, time) VALUES (UPPER('"+subjectid+"'), UPPER('"+department+"'), UPPER('"+floor+"'), "+number+", "+capacity+", '"+date+"','"+time+"')");
                con.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException(e.toString());
            }finally{
                DBCon.closeCon_sqlite();
            }
            
        }
        return retVal;
    }
    
    public boolean addSchedule_Backup() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isScheduleExsit_Backup()){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO schedule (subjectid, department, floor, number, capacity, date, time) VALUES (UPPER('"+subjectid+"'), UPPER('"+department+"'), UPPER('"+floor+"'), "+number+", "+capacity+", '"+date+"','"+time+"')");
                con.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException(e.toString());
            }finally{
                DBCon.closeCon_Backup_sqlite();
            }
            
        }
        return retVal;
    }
    
    public boolean updateSchedule() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isScheduleExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE schedule SET subjectid='"+subjectid+"', department='"+department+"', floor='"+floor+"', number="+number+", capacity="+capacity+" WHERE date='"+date+"' AND time='"+time+"'");
                con.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                System.out.println("Error: " + e);
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_sqlite();
            }
            
        }
        return retVal;
    }
    
        public boolean updateSchedule_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isScheduleExsit_Backup()){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE schedule SET subjectid='"+subjectid+"', department='"+department+"', floor='"+floor+"', number="+number+", capacity="+capacity+" WHERE date='"+date+"' AND time='"+time+"'");
                con.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                System.out.println("Error: " + e);
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_Backup_sqlite();
            }
            
        }
        return retVal;
    }
    
    
    public boolean deleteScedule() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isScheduleExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("DELETE from schedule WHERE date='"+date+"' AND time='"+time+"'"); 
                con.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                System.out.println("Error: " + e);
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_sqlite();
            }
        }
        return retVal;
    }
    
    public String getSubjectID() throws SQLException, ClassNotFoundException{
        String retVal = "";
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT subjectid FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = rs.getString(1);
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public String getDepartment() throws SQLException, ClassNotFoundException{
        String retVal = "";
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT department FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = rs.getString(1);
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public String getFloor() throws SQLException, ClassNotFoundException{
        String retVal = "";
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT floor FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = rs.getString(1);
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public int getNumber() throws SQLException, ClassNotFoundException{
        int retVal = 0;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT number FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = Integer.parseInt(rs.getString(1));
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public int getCapacity() throws SQLException, ClassNotFoundException{
        int retVal = 0;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT capacity FROM schedule WHERE date='"+date+"' AND time='"+time+"'");
            
            while(rs.next()){
                retVal = Integer.parseInt(rs.getString(1));
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database.");
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    
//    public static void main(String[] args) {
////        Schedule schedule = new Schedule("CS1012","FGS","3",1, 30, "2023-04-17", "08:00 AM - 09:00 AM");
////        try {
////            System.out.println("Exsits? " + schedule.isScheduleExsit());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
////        
////        try {
////            System.out.println("Add new? " + schedule.addSchedule());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
////        
////        try {
////            System.out.println("Update? " + schedule.updateSchedule());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
//        
////        try {
////            System.out.println("Delete? " + schedule.deleteScedule());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
//    }
}
