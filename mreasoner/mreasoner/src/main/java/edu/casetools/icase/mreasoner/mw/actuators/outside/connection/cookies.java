package edu.casetools.icase.mreasoner.mw.actuators.outside.connection;


import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.util.List;

public class cookies {

    /** The Constant URL_STRING. */

   // private final static String URL_STRING = "http://www.google.com";
         static CookieManager cookieManager=null;
         static String SID;
         static String userid;

    public static void initCokies(){

        if (cookieManager==null) {
            cookieManager = new CookieManager();
            CookieHandler.setDefault(cookieManager);

        }

    }

    public static void getCokiesFrom(){
       
        CookieStore cookieStore = cookieManager.getCookieStore();
        List<HttpCookie> cookieList = cookieStore.getCookies();

        for (HttpCookie cookie : cookieList)
        {
         //   Log.v("PUTAS COOOKIESSS: ", cookie.getName() +":"+ cookie.getValue());
            if(cookie.getName()=="SID"){
                SID=cookie.getValue();
            }
            if(cookie.getName()=="userid"){
                userid=cookie.getValue();
            }
        }

    }

    public static String getCookiesTo(){
            return String.join(";",  cookieManager.getCookieStore().getCookies().toString());
    }

    public static Boolean isCookieInit(){
            return cookieManager!=null;
    }


}
