package com.yi.wechat.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.jdbc.log.LogFactory;
import com.yi.wechat.util.SignUtil;

/**
 * Servlet implementation class CoreServle
 */
@WebServlet("/CoreServlet")
public class CoreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public CoreServlet() {
        super();
    }
    
    //token验证只有在微信公众平台配置服务器url的时候才会用到，平时用户使用过程中是不会出发下面的代码的。
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("微信服务器请求已经到达!");
		 // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
            System.out.println("Token验证成功,原路返回echostr为:"+echostr);
        }       
        out.close();
        out = null;
   }

	
	/*这下面的逻辑是其它的工程的，功能为接受微信端输入的内容并自动作出回复
	 * protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // 消息的接收、处理、响应（servlet中只是调用相应类中的方法）
		//每次请求都调用一次servlet
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // 调用核心业务类处理消息，核心业务类调用xml工具类       
        String respXml = CoreService.processRequest(request);

        // 响应消息
        PrintWriter out = response.getWriter();
        out.print(respXml);
        //这里不是往网页输出！而是在手机微信聊天界面输出！！！而且这里的respXml是xml类型的
        out.close();
	}*/

}
