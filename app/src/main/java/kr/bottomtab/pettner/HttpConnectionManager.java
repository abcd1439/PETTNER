/*
 *  다양한 HTTP 통신을 관리하는 객체
 *  author PYO IN SOO
 */
package kr.bottomtab.pettner;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnectionManager {

   /*
    *  Java API  Package를 이용한 통신
    */
   public static HttpURLConnection getHttpURLConnection(String targetURL, String requestMethod){
	   HttpURLConnection httpConnection = null;
	   try{
	      URL url = new URL(targetURL);
       
	      httpConnection = (HttpURLConnection) url.openConnection();
		
	      httpConnection.setDoInput(true);
	      httpConnection.setDoOutput(true);
	      httpConnection.setUseCaches(false);
	      httpConnection.setConnectTimeout(10000);
	      httpConnection.setRequestMethod(requestMethod);
		   //Content-Type에 대한 디폴트값
	    /*  httpConnection.setRequestProperty("Content-Type",
	    		  "application/x-www-form-urlencoded");*/
	   }catch(Exception e){
		  Log.e("DEBUG_TAG", "getHttpURLConnection() -- 에러 발생 -- ", e);
	   }
	   return httpConnection;
   }
}