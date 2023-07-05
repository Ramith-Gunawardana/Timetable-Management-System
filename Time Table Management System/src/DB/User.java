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
public class User {
    private String username;
    private String password;
    private int role;
    
    public User(String username, String password, int role){
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public boolean isUserExsit() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_sqlite();
//            Connection con = DBCon.getCon();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username='"+username+"'"); //changed from BINARY username to username (mysql to sqlite)
            
            while(rs.next()){
                retVal = true;
            }
            st.close();
            rs.close();
            con.close();
        }catch(NullPointerException e){
            System.out.println("Error: " + e);
        }catch(SQLException e){
            throw new SQLException("Can't connect to database." +e);
        }catch(ClassNotFoundException e){
            throw new ClassNotFoundException("Can't connect to database.");
        }finally{
            DBCon.closeCon_sqlite();
//            DBCon.closeCon();
        }
        
        return retVal;
    }
    
    public boolean isUserExsit_Backup() throws SQLException,ClassNotFoundException{
        boolean retVal = false;
        
        try{
            Connection con = DBCon.getCon_Backup_sqlite();
//            Connection con = DBCon.getCon_Backup();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM users WHERE username='"+username+"'");
            
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
//            DBCon.closeCon_Backup();
        }
        
        return retVal;
    }
    
    public boolean isAuthenticated() throws SQLException,ClassNotFoundException{
        User user = new User(this.username, this.password, this.role);
        boolean retVal = false;
        
        if(user.isUserExsit()){
            Password pw = new Password();
            
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT password,salt FROM users WHERE username='"+username+"'");

                String inputPassword = null;
                String dbSalt = null;
                
                while(rs.next()){
                    inputPassword = rs.getString(1);
                    dbSalt = rs.getString(2);
                }
                
                if(pw.isPasswordChecked(pw.getHash(this.password, dbSalt), inputPassword, dbSalt)){
                    retVal = true;
                }
                st.close();
                rs.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_sqlite();
            }
            
        }
        
        return retVal;
    }
    
    public int getRole() throws SQLException,ClassNotFoundException{
        User user = new User(this.username, this.password, this.role);
        int userRole = 1;
        
        if (user.isAuthenticated()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT role FROM users WHERE username='"+user.username+"'");
                
                while(rs.next()){
                    userRole = Integer.parseInt(rs.getString(1));
                }
                st.close();
                rs.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_sqlite();
            }
             
        }
        return userRole;
    }
    
    public String getUsername() throws SQLException,ClassNotFoundException{
        User user = new User(this.username, this.password, this.role);
        String userStr = "";
        
        if (user.isAuthenticated()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery("SELECT username FROM users WHERE username='"+user.username+"'");
                
                while(rs.next()){
                    userStr = rs.getString(1);
                }
                st.close();
                rs.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException("Can't connect to database.");
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException("Can't connect to database.");
            }finally{
                DBCon.closeCon_sqlite();
            }
             
        }
        return userStr;
    }
    
    public boolean addUser() throws SQLException,ClassNotFoundException{
        User user = new User(this.username,this.password,this.role);
        boolean retVal = false;
        
        if(!user.isUserExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                Password pw = new Password();
                String salt = pw.getSalt();
                String hash = pw.getHash(user.password, salt);

                st.executeUpdate("INSERT INTO users (username,password,salt,role) VALUES ('"+user.username+"', '"+hash+"', '"+salt+"' , "+user.role+")");
                con.commit();
                //backup
                Connection conBackup = DBCon.getCon_Backup_sqlite();
                Statement stBackup = conBackup.createStatement();
                
                stBackup.executeUpdate("INSERT INTO users (username,password,salt,role) VALUES ('"+user.username+"', '"+hash+"', '"+salt+"' , "+user.role+")");
                conBackup.commit();
                retVal = true;
                st.close();
                con.close();
            }catch(SQLException e){
                throw new SQLException(e);
            }catch(ClassNotFoundException e){
                throw new ClassNotFoundException(e.toString());
            }finally{
                DBCon.closeCon_sqlite();
                DBCon.closeCon_Backup_sqlite();
            }
        }else{
            System.out.println("User already exsit!");
        }
        return retVal;
    }
    
    
    
    public boolean updateUser() throws SQLException,ClassNotFoundException{
        User user = new User(this.username,this.password,this.role);
        boolean retVal = false;
        
        if(user.isUserExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                Password pw = new Password();
                String salt = pw.getSalt();
                String hash = pw.getHash(user.password, salt);

                st.executeUpdate("UPDATE users SET password = '"+hash+"', salt = '"+salt+"',role = "+user.role+" WHERE username = '"+user.username+"'");
                con.commit();
                //backup
                Connection conBackup = DBCon.getCon_Backup_sqlite();
                Statement stBackup = conBackup.createStatement();
                stBackup.executeUpdate("UPDATE users SET password = '"+hash+"', salt = '"+salt+"',role = "+user.role+" WHERE username = '"+user.username+"'");
                conBackup.commit();
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
                DBCon.closeCon_Backup_sqlite();
            }
        }
        return retVal;
    }
    
    
    
    public boolean deleteUser() throws SQLException,ClassNotFoundException{
        User user = new User(this.username,this.password,this.role);
        boolean retVal = false;
        
        if(user.isUserExsit()){
            try{
                Connection con = DBCon.getCon_sqlite();
                Statement st = con.createStatement();

                Password pw = new Password();
                String salt = pw.getSalt();
                String hash = pw.getHash(user.password, salt);

                st.executeUpdate("DELETE from users WHERE username = '"+user.username+"'"); 
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
    
    public static void main(String[] args) {
        User user = new User("Admin", "123", 1);
        try {
            System.out.println("Exsist? :"+user.isUserExsit_Backup());
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex);
        }
        
//        try {
//            System.out.println(user.isAuthenticated());
//        } catch (SQLException | ClassNotFoundException ex) {
//            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//        User u = new User("Staff", "1234", 3);
//        try {
//            u.deleteUser();
//        } catch (SQLException | ClassNotFoundException ex) {
//            System.out.println("E - " + ex);
//        }
        
    }
}
