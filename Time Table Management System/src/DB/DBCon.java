package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Ramith Gunawardana
 */
public class DBCon {
//    mysql
//    private static Connection con;
//    private static Connection conBackup;
    
//    sqlite
    private static Connection c = null;
    private static Connection cb = null;
    
//    mysql dbs
//    private static String url = "jdbc:mysql://localhost:3307/timetable";
//    private static String urlBackup = "jdbc:mysql://localhost:3307/backup_timetable";
//    private static String userName = "root";
//    private static String password = "";
    
    
//   SQLITE DB CONNECTIONS 
    
    //main table
    public static Connection getCon_sqlite() throws SQLException,ClassNotFoundException{
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:src/DB/timetable.db");
            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
        } catch ( Exception e ) {
            System.out.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return c;
    }
    //backup table
    public static Connection getCon_Backup_sqlite() throws SQLException,ClassNotFoundException{
        try {
            Class.forName("org.sqlite.JDBC");
            cb = DriverManager.getConnection("jdbc:sqlite:src/DB/timetable_backup.db");
            cb.setAutoCommit(false);
//            System.out.println("Opened database successfully");
        } catch ( Exception e ) {
            System.out.println( e.getClass().getName() + ": " + e.getMessage() );
        }

        return cb;
    }
    
    public static void closeCon_sqlite(){
        if(c!=null){
            try{
                c.close();
            }catch(SQLException e){
                System.out.print("Error while closing database connection: " + e);
            }
        }
    }
    public static void closeCon_Backup_sqlite(){
        if(cb!=null){
            try{
                cb.close();
            }catch(SQLException e){
                System.out.print("Error while closing database connection: " + e);
            }
        }
    }
    
    
//    MYSQL DB CONNECTIONS
    
//    public static Connection getCon() throws SQLException,ClassNotFoundException{
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        }catch(ClassNotFoundException e){
//            throw new ClassNotFoundException();
//        }
//        try{
//            con = DriverManager.getConnection(url,userName,password);
//        }catch(SQLException e){
//            throw new SQLException();
//        }
//        
//        return con;
//    }
    
//    public static Connection getCon_Backup() throws SQLException,ClassNotFoundException{
//        try{
//            Class.forName("com.mysql.cj.jdbc.Driver");
//        }catch(ClassNotFoundException e){
//            throw new ClassNotFoundException();
//        }
//        try{
//            conBackup = DriverManager.getConnection(urlBackup,userName,password);
//        }catch(SQLException e){
//            throw new SQLException();
//        }
//        
//        return conBackup;
//    }
//    
    
    
//    public static void closeCon(){
//        if(con!=null){
//            try{ 
//                con.close();
//            }catch(SQLException e){
//                System.out.print("Error while closing database connection: " + e);
//            }
//        }
//    }
    
//    public static void closeCon_Backup(){
//        if(conBackup!=null){
//            try{
//                conBackup.close();
//            }catch(SQLException e){
//                System.out.print("Error while closing database connection: " + e);
//            }
//        }
//    }
    
    
    
}
