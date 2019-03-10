package world.ouer.rss;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pc on 2019/3/10.
 */

public class RssUtils {

    private  static String toHexString(byte[] b){

        StringBuilder sb =new StringBuilder();
        for(int i=0;i<b.length;i++){
            sb.append(byteToHexStr(b[i]));
        }
        return sb.toString();
    }

    public  static String  sha1(String msg)  {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(msg.getBytes());
        byte[] digest =md.digest();
        return toHexString(digest);
    }

    private static String byteToHexStr(byte b){
        String hexStr=Integer.toHexString(b & 0xFF);
        if(hexStr.length()==1){
            hexStr="0"+hexStr;
        }
        return hexStr;
    }


    public static  String sha1(InputStream in){

        final int BUFFER_SIZE=1024;
        try {
            MessageDigest md=MessageDigest.getInstance("SHA-1");
            byte[] buffer =new byte[BUFFER_SIZE];
            while(in.read(buffer)!=-1){
                md.update(buffer);
            }
            in.close();
            byte[] digest =md.digest();
            return toHexString(digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
