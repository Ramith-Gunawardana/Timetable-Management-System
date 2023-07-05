package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ramith Gunawardana
 */
public class Resource {
    private String department, floor, name = "";
    private int number, qty = 0;

    public Resource(String department, String floor, int number, String name, int qty) {
        this.department = department;
        this.floor = floor;
        this.number = number;
        this.name = name;
        this.qty = qty;
    }
    
    public boolean isResourceExsit(String name) throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM resource WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+" AND name='"+name+"'");
            
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
    
    public boolean isResourceExsit_Backup(String name) throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT name FROM resource WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+" AND name='"+name+"'");
            
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
    
    
    public boolean addResource() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isResourceExsit(name)){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO resource (department, floor, number, name, qty) VALUES (UPPER('"+department+"'), UPPER('"+floor+"'), "+number+", '"+name+"',"+qty+")");
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
    
    public boolean addResource_Backup() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isResourceExsit_Backup(name)){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("INSERT INTO resource (department, floor, number, name, qty) VALUES (UPPER('"+department+"'), UPPER('"+floor+"'), "+number+", '"+name+"',"+qty+")");
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
    
    public boolean updateResource() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isResourceExsit(name)){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE resource SET qty='"+qty+"' WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+" AND name='"+name+"'");
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
    
    public boolean updateResource_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isResourceExsit_Backup(name)){
            try{
                Connection con = DBCon.getCon_Backup_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("UPDATE resource SET qty='"+qty+"' WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+" AND name='"+name+"'");
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
    
    
    public boolean deleteResource() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isResourceExsit(name)){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("DELETE from resource WHERE department='"+department+"' AND floor='"+floor+"' AND number="+number+" AND name='"+name+"'"); 
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
    
    public String getName(){
        return this.name;
    }
//    public static void main(String[] args) {
////        Resource res = new Resource("FGS","3", 1, "Projector", 6);
////        try {
////            System.out.println("Exsits? " + res.isResourceExsit(res.getName()));
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
////        try {
////            System.out.println("New? " + res.addResource());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
////        try {
////            System.out.println("Update? " + res.updateResource());
////            System.out.println("Update? " + res.updateResource_Backup());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
////        try {
////            System.out.println("Delete? " + res.deleteResource());
////        } catch (SQLException | ClassNotFoundException ex) {
////            System.out.println("Error: " + ex);
////        }
//    }
}
