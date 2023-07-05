package DB;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Ramith Gunawardana
 */
public class Subject {
    private String subjectid,module,facultyid = "";
    
    public Subject(String subjectid, String module, String facultyid){
        this.subjectid = subjectid;
        this.module = module;
        this.facultyid = facultyid;
    }
    
    public boolean isSubjectExsit() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM subject WHERE subjectid='"+subjectid+"'");
            
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
    
    public boolean isSubjectExsit_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM subject WHERE subjectid='"+subjectid+"'");
            
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
    
    public boolean addSubject() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isSubjectExsit()){
            if(isFacMember()){
                try{
                    Connection con = DBCon.getCon_sqlite();
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO subject (subjectid,module,facultyid) VALUES ('"+subjectid+"', '"+module+"', '"+facultyid+"')");
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
            }else{
                System.out.println("Not a faculty member");
            }
            
        }else{
            System.out.println("Classroom already exsit!");
        }
        return retVal;
    }
    
    public boolean addSubject_Backup() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        if(!isSubjectExsit_Backup()){
            if(isFacMember()){
                try{
                    Connection con = DBCon.getCon_Backup_sqlite();
                    Statement st = con.createStatement();

                    st.executeUpdate("INSERT INTO subject (subjectid,module,facultyid) VALUES ('"+subjectid+"', '"+module+"', '"+facultyid+"')");
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
            }else{
                System.out.println("Not a faculty member");
            }
            
        }else{
            System.out.println("Classroom already exsit!");
        }
        return retVal;
    }
    
    public boolean updateSubject() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isSubjectExsit()){
            if(isFacMember()){
                try{
                    Connection con = DBCon.getCon_sqlite();
                    Statement st = con.createStatement();

                    st.executeUpdate("UPDATE subject SET module='"+module+"', facultyid='"+facultyid+"' WHERE subjectid='"+subjectid+"'");
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
            }else{
                 System.out.println("Not a faculty member");
            }
            
        }
        return retVal;
    }
    
    public boolean updateSubject_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isSubjectExsit_Backup()){
            if(isFacMember()){
                try{
                    Connection con = DBCon.getCon_Backup_sqlite();
                    Statement st = con.createStatement();

                    st.executeUpdate("UPDATE subject SET module='"+module+"', facultyid='"+facultyid+"' WHERE subjectid='"+subjectid+"'");
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
            }else{
                 System.out.println("Not a faculty member");
            }
            
        }
        return retVal;
    }
    
    public boolean deleteSubject() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        if(isSubjectExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                st.executeUpdate("DELETE FROM subject WHERE subjectid='"+subjectid+"'");
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
    
    public boolean isFacMember() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT role FROM users WHERE  username='"+facultyid+"'");
            
            while(rs.next()){
                if(Integer.valueOf(rs.getString(1))==1 || Integer.valueOf(rs.getString(1))==2)
                retVal = true;
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database."+ e);
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database." + e);
        }finally{
            DBCon.closeCon_sqlite();
        }
        
        return retVal;
    }
    
    public boolean isFacMember_Backup() throws SQLException, ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT role FROM users WHERE  username='"+facultyid+"'");
            
            while(rs.next()){
                if(Integer.valueOf(rs.getString(1))==1 || Integer.valueOf(rs.getString(1))==2)
                retVal = true;
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database."+ e);
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database." + e);
        }finally{
            DBCon.closeCon_Backup_sqlite();
        }
        
        return retVal;
    }
    
    public String getSubjectName() throws SQLException, ClassNotFoundException{
        String retVal = "";
        
        try{
            Connection con = DBCon.getCon_sqlite();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT module FROM subject WHERE subjectid='"+subjectid+"'");
            
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
    
    public static void main(String[] args) {
        Subject subject = new Subject("SUB1","SubjectName","Dhanuka");
        try {
            System.out.println("Exsits? " + subject.isSubjectExsit());
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
//        try {
//            System.out.println("Fac member? " + subject.isFacMember());
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Error: " + ex);
//        }
//        try {
//            System.out.println("Add new? " + subject.addSubject());
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Error: " + ex);
//        }
//        
//        try {
//            System.out.println("Update? " + subject.updateSubject());
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("Error: " + ex);
//        }
        
        try {
            System.out.println("Delete? " + subject.deleteSubject());
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println("Error: " + ex);
        }
    }
}
