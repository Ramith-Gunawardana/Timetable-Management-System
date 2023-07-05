package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ramith Gunawardana
 */
public class Classroom {
    private String department,floor = "";
    private int number,capacity = 0;
    
    public Classroom(String department, String floor, int number, int capacity){
        this.department = department;
        this.floor = floor;
        this.number = number;
        this.capacity = capacity;
    }
    
    public boolean isClassroomExsit() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM classroom WHERE department='"+department+"' and  floor='"+floor+"' and number='"+number+"'");
            
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
    
    public boolean isClassroomExsit_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM classroom WHERE department='"+department+"' and  floor='"+floor+"' and number="+number+"");
            
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
    
    public boolean addClassroom() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isClassroomExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO classroom (department,floor,number,capacity) VALUES ('"+department+"' , '"+floor+"', "+number+" , "+capacity+")");
                con.commit();
                st.close();
                con.close();
                retVal = true;
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException(e.toString());
            }finally{
                DBCon.closeCon_sqlite();
            }
        }else{
            System.out.println("Classroom already exsit!");
        }
        return retVal;
    }
    
    public boolean addClassroom_Backup() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isClassroomExsit_Backup()){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO classroom (department,floor,number,capacity) VALUES ('"+department+"' , '"+floor+"', "+number+" , "+capacity+")");
                con.commit();
                st.close();
                con.close();
                retVal = true;
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException(e.toString());
            }finally{
                DBCon.closeCon_Backup_sqlite();
            }
        }else{
            System.out.println("Classroom already exsit!");
        }
        return retVal;
    }
    
    public boolean updateClassroom() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isClassroomExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE classroom SET capacity='"+capacity+"' WHERE department='"+department+"' and floor='"+floor+"' and number="+number+"");
                con.commit();
                st.close();
                con.close();
                retVal = true;
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
    
    public boolean updateClassroom_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isClassroomExsit_Backup()){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE classroom SET capacity='"+capacity+"' WHERE department='"+department+"' and floor='"+floor+"' and number="+number+"");
                con.commit();
                st.close();
                con.close();
                retVal = true;
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
    
    public boolean deleteClassroom() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isClassroomExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("DELETE from classroom WHERE department='"+department+"' and floor='"+floor+"' and number="+number+""); 
                con.commit();
                st.close();
                con.close();
                retVal = true;
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
    
    public static void main(String[] args) {
        Classroom classroom = new Classroom("FGS", "3", 2, 80);
        try {
            System.out.println("Exsits? " + classroom.isClassroomExsit());
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
        try {
            System.out.println("Add new? " + classroom.addClassroom());
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
        
//        try {
//            System.out.println("Update? " + classroom.updateClassroom());
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Error: " + ex);
//        }
        
//        try {
//            System.out.println("Delete? " + classroom.deleteClassroom());
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Error: " + ex);
//        }
    }
}
