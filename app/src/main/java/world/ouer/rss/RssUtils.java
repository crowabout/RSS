package world.ouer.rss;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by pc on 2019/3/10.
 */

public class RssUtils {

    private static final String TAG = "RSSUtils";

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

    public static String toMb(int frombyte){
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
        df.applyPattern(".00");
        double format =frombyte/Math.pow(2,20);
        return df.format(format)+"M";
    }


    public static String extractFileTypeFromUrl(String url){
        if(TextUtils.isEmpty(url)){
            return "unknown";
        }
        if(url.matches(".*mp3.*")){
            return "mp3";
        }else if(url.matches(".*mp4.*")){
            return "mp4";
        }
        return "unknown";
    }

    public static void creatStorePlace(){

        File storePlace =new File("/mnt/sdcard/ouer.world.rss/");
        String stat =Environment.getExternalStorageState();
        if(stat.equalsIgnoreCase(Environment.MEDIA_MOUNTED)){
            if(storePlace.mkdirs()){
                Log.d(TAG, "creatStorePlace: mkdir ok");
            }else{
                Log.d(TAG, "creatStorePlace: mkdir fail");
            }
        }
    }

    public static  String dropHtmlTagFromStr(String str){
        String regex ="(<.*>)";
        Pattern p =Pattern.compile(regex,Pattern.MULTILINE);
        Matcher m =p.matcher(str);
        String s =m.replaceAll("");
        return s.trim();
    }


    public static String guessTypeFromUrl(String url){
        String type =extractFileTypeFromUrl(url);
        if(type.equalsIgnoreCase("unknown")){
            return "txt";
        }
        return type;
    }


    public static String splitTime(String time,String pattern){

        if (TextUtils.isEmpty(time)) {
            return "noTime";
        }

        String prn="EEE, dd MMM yyyy HH:mm:ss Z";
        SimpleDateFormat sdf=new SimpleDateFormat(prn, Locale.US);
        Date date;
        try {
            date =sdf.parse(time);
        } catch (ParseException e) {
            Log.d(TAG, "splitTime: "+e.getMessage());
            return time;
        }
        if(TextUtils.isEmpty(pattern)){
            sdf.applyPattern("HH:mm/dd/MM");
        }else{
            sdf.applyPattern(pattern);
        }
        return sdf.format(date);
    }


    public static String channelSimplify(String channel){
        String ss60="60-Second Science";
        String CNN="CNN";
        String reuter="Reuters News";
        String sci="Science";
        if(channel.contains(ss60)){
            return "SAm";
        }
        if(channel.contains(CNN)){
            return "CNN";
        }
        if (channel.contains(sci)) {
            return "Sci";
        }
        if(channel.contains(reuter)){
            return "RET";
        }
        return "x";
    }

}
