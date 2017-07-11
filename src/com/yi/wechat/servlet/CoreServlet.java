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
    
    //token��ֻ֤����΢�Ź���ƽ̨���÷�����url��ʱ��Ż��õ���ƽʱ�û�ʹ�ù������ǲ����������Ĵ���ġ�
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("΢�ŷ����������Ѿ�����!");
		 // ΢�ż���ǩ��
        String signature = request.getParameter("signature");
        // ʱ���
        String timestamp = request.getParameter("timestamp");
        // �����
        String nonce = request.getParameter("nonce");
        // ����ַ���
        String echostr = request.getParameter("echostr");

        PrintWriter out = response.getWriter();
        
        // ͨ������signature���������У�飬��У��ɹ���ԭ������echostr����ʾ����ɹ����������ʧ��
        if (SignUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
            System.out.println("Token��֤�ɹ�,ԭ·����echostrΪ:"+echostr);
        }       
        out.close();
        out = null;
   }

	
	/*��������߼��������Ĺ��̵ģ�����Ϊ����΢�Ŷ���������ݲ��Զ������ظ�
	 * protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // ��Ϣ�Ľ��ա�������Ӧ��servlet��ֻ�ǵ�����Ӧ���еķ�����
		//ÿ�����󶼵���һ��servlet
        // ��������Ӧ�ı��������ΪUTF-8����ֹ�������룩
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ���ú���ҵ���ദ����Ϣ������ҵ�������xml������       
        String respXml = CoreService.processRequest(request);

        // ��Ӧ��Ϣ
        PrintWriter out = response.getWriter();
        out.print(respXml);
        //���ﲻ������ҳ������������ֻ�΢���������������������������respXml��xml���͵�
        out.close();
	}*/

}
