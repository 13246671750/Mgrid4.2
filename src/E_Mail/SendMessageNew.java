package E_Mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMessageNew {

	public  String mailProtocol="smtp";
	public  String myEmailSMTPHost = "smtp.qq.com";
	public  String myEmailAccount = "453938089@qq.com";
	public  String myEmailPassword = "sgipglsayogvcaih";	
	public  String receiveMailAccount = "leisiyang521@163.com";
	
	public  String Subject="����";
	public  String fromName="����������";
	public  String toName="�ռ�������";
	public  String content="����";
	
	
	public String getMailProtocol() {
		return mailProtocol;
	}

	public void setMailProtocol(String mailProtocol) {
		this.mailProtocol = mailProtocol;
	}

	public String getMyEmailSMTPHost() {
		return myEmailSMTPHost;
	}

	public void setMyEmailSMTPHost(String myEmailSMTPHost) {
		this.myEmailSMTPHost = myEmailSMTPHost;
	}

	public String getMyEmailAccount() {
		return myEmailAccount;
	}

	public void setMyEmailAccount(String myEmailAccount) {
		this.myEmailAccount = myEmailAccount;
	}

	public String getMyEmailPassword() {
		return myEmailPassword;
	}

	public void setMyEmailPassword(String myEmailPassword) {
		this.myEmailPassword = myEmailPassword;
	}

	public String getReceiveMailAccount() {
		return receiveMailAccount;
	}

	public void setReceiveMailAccount(String receiveMailAccount) {
		this.receiveMailAccount = receiveMailAccount;
	}

	public String getSubject() {
		return Subject;
	}

	public void setSubject(String subject) {
		Subject = subject;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public String getToName() {
		return toName;
	}

	public void setToName(String toName) {
		this.toName = toName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}


	
	
	
	public  void  sendMessage()
	{
		System.out.println("��ʼ��");
		Properties props = new Properties();                    // ��������
		
		
		
        props.setProperty("mail.transport.protocol",mailProtocol);   // ʹ�õ�Э�飨JavaMail�淶Ҫ��
        props.setProperty("mail.smtp.host", myEmailSMTPHost);   // �����˵������ SMTP ��������ַ
        props.setProperty("mail.smtp.auth", "true");            // ��Ҫ������֤
        props.setProperty("mail.smtp.starttls.enable", "true");

        Session session = Session.getDefaultInstance(props);
        session.setDebug(true);                     
        
        // 3. ����һ���ʼ�
        MimeMessage message;
		try {
			message = createMimeMessage(session, myEmailAccount, receiveMailAccount);
			   // 4. ���� Session ��ȡ�ʼ��������
	        Transport transport = session.getTransport();

	        transport.connect(myEmailAccount, myEmailPassword);

	        // 6. �����ʼ�, �������е��ռ���ַ, message.getAllRecipients() ��ȡ�������ڴ����ʼ�����ʱ��ӵ������ռ���, ������, ������
	        transport.sendMessage(message, message.getAllRecipients());

	        // 7. �ر�����
	        transport.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    				
	}
	
	public  MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
	        // 1. ����һ���ʼ�
	        MimeMessage message = new MimeMessage(session);

	        // 2. From: �����ˣ��ǳ��й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸��ǳƣ�
	        message.setFrom(new InternetAddress(sendMail,fromName, "UTF-8"));

	        // 3. To: �ռ��ˣ��������Ӷ���ռ��ˡ����͡����ͣ�
	        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(receiveMail, toName, "UTF-8"));

	        // 4. Subject: �ʼ����⣨�����й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸ı��⣩
	        message.setSubject(Subject, "UTF-8");

	        // 5. Content: �ʼ����ģ�����ʹ��html��ǩ���������й�����ɣ����ⱻ�ʼ�����������Ϊ���ķ������������ʧ�ܣ����޸ķ������ݣ�
	 //       message.setContent("XX�û����, ����ȫ��5��, ��������, ��������ٵ�һ�ꡣ����", "text/html;charset=UTF-8");
	        message.setText(content);

	        // 6. ���÷���ʱ��
	        message.setSentDate(new Date());

	        // 7. ��������
	        message.saveChanges();

	        return message;
	}
}
