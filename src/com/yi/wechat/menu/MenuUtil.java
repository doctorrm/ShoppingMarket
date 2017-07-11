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
        // ƴװ�����˵���url
        String url = menu_create_url.replace("ACCESS_TOKEN", accessToken.getToken());
        // ���˵�����ת����json�ַ���
        String jsonMenu = JSONObject.fromObject(menu).toString();
        System.out.println(jsonMenu);
        // ���ýӿڴ����˵�������Ľӿڸ�����һ��url��
        JSONObject jsonObject = httpRequest(url, "POST", jsonMenu);
        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                log.error("�����˵�ʧ�� errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }else{
            	log.info("�����˵��ɹ���");
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
				log.error("��ȡaccess_tokenʧ��  errcode:{} errmsg{}",jsonToken.getInt("errcode"),jsonToken.getString("errmsg"));
			}
		}
		return access_token;
	}
	
	public static JSONObject httpRequest(String request_url,String request_method,String commit_msg){
		JSONObject jsonObject=null;
		StringBuffer buffer=new StringBuffer();
		try{
			//֤������
			TrustManager[] tm={new MyX509TrustManager()};
			SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
			sslContext.init(null,tm,new java.security.SecureRandom());			
			SSLSocketFactory ssf=sslContext.getSocketFactory();
			//��ȡ���Ӷ���
			URL url=new URL(request_url);
			HttpsURLConnection httpsUrlCon=(HttpsURLConnection) url.openConnection();
			//�ü���set
			httpsUrlCon.setSSLSocketFactory(ssf);
			httpsUrlCon.setDoInput(true);
			httpsUrlCon.setDoOutput(true);
			httpsUrlCon.setUseCaches(true);
			httpsUrlCon.setRequestMethod(request_method);
			//������ʽ���д���
			if("GET".equalsIgnoreCase(request_method)){
				httpsUrlCon.connect();
			}
			if(null!=commit_msg){
				OutputStream outputStream=httpsUrlCon.getOutputStream();
				outputStream.write(commit_msg.getBytes("utf-8"));
				outputStream.close();
			}
			//�Է��ص�InputStream���д���
			InputStream inputStream=httpsUrlCon.getInputStream();
			InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"utf-8");
			BufferedReader bufferReader=new BufferedReader(inputStreamReader);
			String str=null;
			while(null!=(str=bufferReader.readLine())){
				buffer.append(str);
			}
			jsonObject =JSONObject.fromObject(buffer.toString());	
			//�ر���Դ
			bufferReader.close();
			inputStreamReader.close();
			inputStream.close();
			httpsUrlCon.disconnect();
			//�����쳣
		}catch(ConnectException ce){
			log.error("Wechat server connection timed out!");
		}catch(Exception e){
			log.error("Https request error:{}",e);
		}		
		return  jsonObject;
	}
}
