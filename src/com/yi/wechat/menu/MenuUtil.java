package com.yi.wechat.menu;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yi.wechat.util.MyX509TrustManager;
import com.yi.wechat.menu.Menu;
import com.yi.wechat.pojo.AccessToken;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

public class MenuUtil {
	private static Logger log=LoggerFactory.getLogger(MenuUtil.class);
	private String access_token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	private static String menu_create_url="https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	
	public static int createMenu(Menu menu, AccessToken accessToken) {
        int result = 0;
        // 拼装创建菜单的url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken.getToken());
        // 将菜单对象转换成json字符串
        String jsonMenu = JSONObject.fromObject(menu).toString();
        System.out.println(jsonMenu);
        // 调用接口创建菜单，这里的接口更像是一个url。
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }else{
            	log.info("创建菜单成功！");
            }
        }

        return result;
    }
	
	public AccessToken getAccessToken(String appId,String appSecret){
		AccessToken access_token=null;
		String accessTokenUrl=access_token_url.replace("APPID",appId).replace("APPSECRET",appSecret);
		JSONObject jsonToken=httpRequest(accessTokenUrl,"GET",null);
		
		if(jsonToken!=null){
			try{
			access_token=new AccessToken();
			access_token.setExpiresIn(jsonToken.getInt("expires_in"));
			access_token.setToken(jsonToken.getString("access_token"));
			}catch(JSONException e){
				access_token=null;
				log.error("获取access_token失败  errcode:{} errmsg{}",jsonToken.getInt("errcode"),jsonToken.getString("errmsg"));
			}
		}
		return access_token;
	}
	
	public static JSONObject httpRequest(String request_url,String request_method,String commit_msg){
		JSONObject jsonObject=null;
		StringBuffer buffer=new StringBuffer();
		try{
			//证书设置
			TrustManager[] tm={new MyX509TrustManager()};
			SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null,tm,new java.security.SecureRandom());			
			SSLSocketFactory ssf=sslContext.getSocketFactory();
			//获取链接对象
			URL url=new URL(request_url);
			HttpsURLConnection httpsUrlCon=(HttpsURLConnection) url.openConnection();
			//好几个set
			httpsUrlCon.setSSLSocketFactory(ssf);
			httpsUrlCon.setDoInput(true);
			httpsUrlCon.setDoOutput(true);
			httpsUrlCon.setUseCaches(true);
			httpsUrlCon.setRequestMethod(request_method);
			//对请求方式进行处理。
			if("GET".equalsIgnoreCase(request_method)){
				httpsUrlCon.connect();
			}
			if(null!=commit_msg){
				OutputStream outputStream=httpsUrlCon.getOutputStream();
				outputStream.write(commit_msg.getBytes("utf-8"));
				outputStream.close();
			}
			//对返回的InputStream进行处理。
			InputStream inputStream=httpsUrlCon.getInputStream();
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferReader=new BufferedReader(inputStreamReader);
			String str=null;
			while(null!=(str=bufferReader.readLine())){
				buffer.append(str);
			}
			jsonObject =JSONObject.fromObject(buffer.toString());	
			//关闭资源
			bufferReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpsUrlCon.disconnect();
			//捕获异常
		}catch(ConnectException ce){
			log.error("Wechat server connection timed out!");
		}catch(Exception e){
			log.error("Https request error:{}",e);
		}		
		return  jsonObject;
	}
}
