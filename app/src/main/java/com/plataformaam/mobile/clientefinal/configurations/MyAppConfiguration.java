package com.plataformaam.mobile.clientefinal.configurations;

import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bernard on 04/01/2015.
 */

public class MyAppConfiguration {
    //GAMBIARRA :: LOGIN PARA CRIACAO DE USUÀRIO VIA API
    private final String loginBase = "root";
    private final String passwordBase = "qw";

    public static class Preferences {
        public final static String PREFERENCE_AUTO_LOGIN 	= 	"PREFERENCE_AUTO_LOGIN";
        public final static String HTTP_X_REST_USERNAME 	= 	"HTTP_X_REST_USERNAME";
        public final static String HTTP_X_REST_PASSWORD 	= 	"HTTP_X_REST_PASSWORD";

    }

    public static class BroadCastMessage{
        public final static String LOGIN_DONE               =   "LOGIN_DONE";
        public final static String AUTO_LOGIN_DONE          =   "AUTO_LOGIN_DONE";
        public final static String SERVICE_READY            =   "SERVICE_READY";
        public final static String JOIN_VCLOC               =   "JOIN_VCLOC";
        public final static String LEAVE_VCLOC              =   "LEAVE_VCLOC";
    }


    public static class UserPositionContent{
        public final static String LOGIN_POSITION           =   "login_position";
        public final static String NAVIGATE                 =   "navigate";
        public final static String INSIDE_VCLOC             =   "inside_vcloc";
        public final static String JOIN_VCLOC               =   "join_vcloc";
        public final static String LEAVE_VCLOC              =   "leave_vcloc";
    }



    private String  webService = "http://api.plataformaam.com/v1/index.php/api/";
    private static MyAppConfiguration globalConfiguration;

    public MyAppConfiguration() {
    }


    public static MyAppConfiguration getInstance(){
        if( MyAppConfiguration.globalConfiguration == null ){
            MyAppConfiguration.globalConfiguration  = new MyAppConfiguration();
        }
        return MyAppConfiguration.globalConfiguration;
    }
    /**
     * @return the webService
     */
    public String getWebService() {
        return webService;
    }

    /**
     * @return the loginBase
     */
    public String getLoginBase() {
        return loginBase;
    }
    /**
     * @return the passwordBase
     */
    public String getPasswordBase() {
        return passwordBase;
    }


    public final static class UpiType_Data_Code {
        public final  static  int UPI_TEXT   = 0;
        public final  static  int UPI_IMAGE  = 1;
        public final  static  int UPI_VIDEO  = 2;
        public final  static  int UPI_AUDIO  = 3;
        public final  static  int UPI_LINK   = 4;
    }

    public static List<UPIType> loadUpiTypes(){
        List<UPIType> upiTypes = new ArrayList<UPIType>();
        upiTypes.add(MyAppConfiguration.UpiType_Data_Code.UPI_TEXT,    new UPIType(1,"UPIText"));
        upiTypes.add(MyAppConfiguration.UpiType_Data_Code.UPI_IMAGE,   new UPIType(2,"UPIImage"));
        upiTypes.add(MyAppConfiguration.UpiType_Data_Code.UPI_VIDEO,   new UPIType(3,"UPIVideo"));
        upiTypes.add(MyAppConfiguration.UpiType_Data_Code.UPI_AUDIO,   new UPIType(4,"UPIAudio"));
        upiTypes.add(MyAppConfiguration.UpiType_Data_Code.UPI_LINK,    new UPIType(5,"UPILink"));
        return upiTypes;
    }

}


