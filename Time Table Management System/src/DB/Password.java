package DB;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 *
 * @author Ramith Gunawardana
 */
public class Password {
    public Password(){
    }
    
    public String getHash(String originalPassword, String salt){
        String hash = null;
        
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] bytes = md.digest(originalPassword.getBytes());
            StringBuilder sb = new StringBuilder();
            for(int i=0; i<bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash = sb.toString();
        }catch(NoSuchAlgorithmException e){
            System.out.println("Error while generating hash : " + e.toString());
        }
        
        return hash;
    }
    
    public String getSalt(){
        String saltStr = null;
        
        try{
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            saltStr = salt.toString();
        }catch(NoSuchAlgorithmException e){
            System.out.println("Error while generating salt : " + e.toString());
        }
        
        return saltStr;
    }
    
    public boolean isPasswordChecked(String input_password,String db_password, String db_salt){
        Password pw = new Password();
        
        String inputHash = pw.getHash(input_password, db_salt);
        String originalHash = pw.getHash(db_password, db_salt);
        
        if(originalHash.equals(inputHash)){
            return true;
        }else{
            return false;
        }
        
    }
    
}
