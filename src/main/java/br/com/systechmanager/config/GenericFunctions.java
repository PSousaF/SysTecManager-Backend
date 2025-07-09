package br.com.systechmanager.config;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Appender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

@Service
public class GenericFunctions {


	@Value("${log.file.save}")
	private static String logFile;
	

	  @Value("${email.pass.access}")
	  private String psw;
	  //final String psw="1019flapE";
	  
	  
	public HashMap<String, Object> getStatusObject(Object data, Boolean status) {
		HashMap<String, Object> returnData = new HashMap<>();
	    ArrayList<Object> returnItem = new ArrayList<Object>();
	  //  if(!ObjectUtils.isEmpty(data))
	    	returnItem.add(data);
		if (status){
			returnData.put("success", true);
			returnData.put("data", data);
		} 
		else {
			returnData.put("success", false);
			returnData.put("data", returnItem);
		}
		return returnData;
	}
	
	public static String generateUuid() {
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        return uuidAsString;
    }
	

    public static void log(String log_string) {  //, int status
    		int status = 3;
          Logger logger = Logger.getLogger("MyLog");  //fine
          Appender fh = null;
          try {
              Date data = new Date();
              SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
              String format = formatador.format(data);
              SimpleDateFormat dataFormat = new SimpleDateFormat("dd-MM-yyyy");
              String formatFile = "test " + dataFormat.format(data);
              fh = new FileAppender(new SimpleLayout(), logFile + formatFile);
              logger.addAppender(fh);
              fh.setLayout(new SimpleLayout());
              logger.info(format + " - " + log_string);
          	switch(status) {
	      	  case 0:
	              logger.fatal(format + " - " + log_string);
	      	    break;
	      	  case 1:
	              logger.error(format + " - " + log_string);
	      	    break;
	      	  default:
	              logger.info(format + " - " + log_string);;
	      	}
          } catch (SecurityException e) {
        	  System.out.println("Erro Security Log - " + e.getMessage());
              e.printStackTrace();
          } catch (IOException e) {
        	  System.out.println("Erro IOException Log - " + e.getMessage());
              e.printStackTrace();
          }
  }
	
	public Image getWatermarkedTopic(PdfContentByte cb, Image img, String watermark, Font font)
		    throws DocumentException {
	    float width = img.getScaledWidth();
	    float height = img.getScaledHeight();
	    PdfTemplate template = cb.createTemplate(width, height);
	    template.addImage(img, width, 0, 0, height, 0, 0);
	    ColumnText.showTextAligned(template, Element.ALIGN_CENTER,
	            new Phrase(watermark, font), width / 2, height / 4, 0);
	    return Image.getInstance(template);
		}

	public void converteBase64ToImg(String route, String nameImg, String imgBase64) {
		System.out.println("Convertendo imagem " + nameImg);
		String[] strings = imgBase64.split(",");

		String extension;
		switch (strings[0]) {
		case "data:image/jpeg;base64":
			extension = "jpeg";
			break;
		case "data:image/png;base64":
			extension = "png";
			break;
		default:
			extension = "png";
			break;
		}
		byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
		String path = route + nameImg + "." + extension;
		File file = new File(path);
		try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
			outputStream.write(data);
			outputStream.close();
			System.out.println(path + " convertida com sucesso");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Falha ao converter imagem " + e.getMessage());
		}
	}
	

	public int imgWidth(String foto, int z, String route) {
		System.out.println("Verificando tamanho da imagem (Consultoria) " + foto);
		String localImg = route + foto;
		try {
			BufferedImage bimg = ImageIO.read(new File(localImg));
			int heighth = bimg.getHeight();
			if (heighth > 960)
				if(z == 1)
					return 28;
				else
					return 50;
			else
				if(z == 1)
					return 50;
				else
					return 80;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Falha ao verificar tamanho da imagem " + e.getMessage());
			return 50;
		}
	}
	
	public int imgWidthAudit(String foto, int z, String route) {
		System.out.println("Verificando tamanho da imagem (Auditoria) " + foto);
		String localImg = route + foto;
		try {
			BufferedImage bimg = ImageIO.read(new File(localImg));
			int heighth = bimg.getHeight();
			if (heighth > 960)
				if(z == 1)
					return 20;
				else
					return 35;
			else
				if(z == 1)
					return 35;
				else
					return 75;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Falha ao verificar tamanho da imagem " + e.getMessage());
			return 35;
		}
	}
	
	public boolean sendEmailPDF(String email, String data, String route, String fileName) {
		  String to2="relatorios@consuqualy.com.br";
		  final String user="ConsuQualy <relatorios@consuqualy.com.br>"; 
		  final String usr="relatorios@consuqualy.com.br"; 
		  
		  final String subject = "ConsuQualy - Relatório da visita - " + data;
	        Properties props = new Properties();
	        props.put("mail.smtp.host", "ns268.hostgator.com.br");
	        props.put("mail.smtp.socketFactory.port", "465");
	        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.port", "26");
	        props.put("mail.mime.charset", "UTF-8");
	        props.put ("mail.smtp.starttls.enable", "true");
	        props.put ("mail.smtp.socketFactory.fallback", "false");

	        try {
	        	Session session = Session.getInstance(props,
		                new javax.mail.Authenticator() {
		                    protected PasswordAuthentication getPasswordAuthentication() {
		                        return new PasswordAuthentication(usr,psw);
		                    }
		                });
	        	
	        	String teste = email;                  //////Retirar depois
	        	email = "pedroo.sousa@outlook.com.br"; //////Retirar depois
	        	
	        	Message message = new MimeMessage(session); 
	        	message.setFrom(new InternetAddress(user)); 
	        	message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
	           // message.setRecipients(Message.RecipientType.CC, to2);
	            message.setSubject(subject);


	        	BodyPart messageBodyPart = new MimeBodyPart();  
	        	messageBodyPart.setText("Segue em anexo o histórico da visita. - " + teste);

	                MimeBodyPart attachmentPart = new MimeBodyPart();
	                try {
						attachmentPart.attachFile(new File(route + fileName + ".pdf"));
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("erro " + e.getMessage());
					}
	                
	                Multipart multiparts = new MimeMultipart();
	                multiparts.addBodyPart(messageBodyPart);
	                multiparts.addBodyPart(attachmentPart);
	                message.setContent(multiparts);

	            System.out.println("Enviando email");
	            Transport.send(message);
	            System.out.println("Email enviado com sucesso para: " + email);
	            return true;
	        }
	        catch(MessagingException e)
	        {
	        	System.out.println(e.getMessage());
	        	return false;
	        }
	}
}
