package com.plataformaam.mobile.clientefinal.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.plataformaam.mobile.clientefinal.helpers.gson.MyFilter;
import com.plataformaam.mobile.clientefinal.helpers.gson.MyFilterItem;
import com.plataformaam.mobile.clientefinal.helpers.gson.adapters.GSonBooleanAsIntAdapter;
import com.plataformaam.mobile.clientefinal.models.vcloc.upi.UPIType;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bernard on 04/01/2015.
 */

public class MyAppConfiguration {
    //GAMBIARRA :: LOGIN PARA CRIACAO DE USUÀRIO VIA API
    private final String loginBase = "base";
    private final String passwordBase = "base";

    public static class LOG{
        public final static String Service              = "SERVICE";
        public final static String ServiceLocation      = "LOCATION";
        public final static String Activity             = "ACTIVITY";
        public final static String Runnable             = "RUNNABLE";
        public final static String Application          = "APPLICATION";
        public final static String Model                = "MODEL";

    }


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

        public final static String INSIDE_VCLOC             =   "inside_vcloc";
        public final static String JOIN_VCLOC               =   "join_vcloc";
        public final static String LEAVE_VCLOC              =   "leave_vcloc";

        public final static String LOGIN_POSITION           =   "login_position";
        public final static String NAVIGATE                 =   "navigate";
        public final static String PUBLISH                  =   "publish";
        public static final String SUBSCRIBE_VCOM           =   "subscribe_vcloc";
    }



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



    public static class EVENT_BUS_MESSAGE{

        //USER SERVICE
        public final static String TRY_AUTO_LOGIN           = "TRY_AUTO_LOGIN";
        public final static String TRY_LOGIN                = "TRY_LOGIN";
        public final static String LOGIN_DONE               = "LOGIN_DONE";
        public final static String LOGIN_FAIL               = "LOGIN_FAIL";

        public static final String TRY_LOGOUT               = "TRY_LOGOUT";
        public final static String LOGOUT_DONE              = "LOGOUT_DONE";


        public final static String UPI_RELOADED             = "UPI_RELOADED";
        public final static String UPI_RELOADED_FAIL        = "UPI_RELOADED_FAIL";
        public final static String SAVE_UPI                 = "SAVE_UPI";
        public final static String DELETE_UPI               = "DELETE_UPI";
        public final static String UPI_OPERATION_FAIL       = "UPI_OPERATION_FAIL";
        public final static String UPI_OPERATION_SUCCESS    = "UPI_OPERATION_SUCCESS";


        //VCOM SERVICE
        public final static String LOAD_COMPOSITE           = "LOAD_COMPOSITE";
        public final static String LOAD_BASE                = "LOAD_BASE";
        public final static String PUBLISH_UPI              = "PUBLISH_UPI";
        public final static String RESPONSE_UPI             = "RESPONSE_UPI";
        public final static String PUBLISH_UPI_FAIL         = "PUBLISH_UPI_FAIL";
        public final static String PUBLISH_UPI_SUCCESS      = "PUBLISH_UPI_SUCCESS";

        public static final String SUBSCRIBE_COMPOSITE      = "SUBSCRIBE_COMPOSITE";
        public static final String SUBSCRIBE_SUCCESS        = "SUBSCRIBE_SUCCESS";
        public static final String SUBSCRIBE_FAIL           = "SUBSCRIBE_FAIL";


        //LOCATION SERVICE
        public final static String LOCATION_CHANGE          = "LOCATION_CHANGE";
        public final static String RELOAD_BASE              = "RELOAD_BASE";
        public final static String GET_PUBLICATIONS         = "GET_PUBLICATIONS";
        public final static String RELOAD_PUBLICATIONS      = "RELOAD_PUBLICATIONS";



    }

    public static class VOLLEY_TAG{
        public final static String LOGIN                = "LOGIN";
        public final static String SAVE_USER_POSITION   = "SAVE_USER_POSITION";
        public final static String MANIPULATE_USER      = "MANIPULATE_USER";
        public final static String MANIPULATE_VCOM      = "MANIPULATE_VCOM";
        public static final String MANIPULATE_UPI       = "MANIPULATE_UPI";

    }



    private String  webService = "http://api.plataformaam.com/v1/index.php/api/";
    /**
     * @return the webService
     */
    public String getWebService() {
        return webService;
    }

    public String prepareWebService(String className,MyFilter filter){
        String request_url = null;
        if( filter != null ) {
            GsonBuilder builder = new GsonBuilder();
            builder.setDateFormat("yyyy-MM-dd HH:mm:ss").registerTypeAdapter(Boolean.class, new GSonBooleanAsIntAdapter()).registerTypeAdapter(boolean.class, new GSonBooleanAsIntAdapter());
            Gson gson = builder.create();
            String jsonFilter = gson.toJson(filter.getFilter(), new TypeToken<List<MyFilterItem>>() {}.getType());
            try {
                request_url = webService + className + "?filter=" + URLEncoder.encode(jsonFilter, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            request_url=webService+className;
        }
        return request_url;
    }
    public String prepareWebService(String className,String filter){
        String request_url = null;
        if( filter != null ) {
            try {
                request_url=webService+className +"?filter="+URLEncoder.encode(filter,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            request_url=webService+className;
        }
        return request_url;
    }

    public String prepareWebService(String className){
        String request_url = webService+className;
        return request_url;
    }


}


