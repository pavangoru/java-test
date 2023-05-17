
package com.sharp.sharp.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.http.MediaType;

import com.sun.mail.util.BASE64DecoderStream;

public class SHARP6MailConstants {

	private static final int BUFFER_SIZE = 4096;

	private static int numberOfParts;

	static String attachFiles = "";

	public static Multipart getAttachmentFromServer(String attachmentPath, String AWSKstorePath) {
		MimeBodyPart messageBodyPart = null;
		Multipart multipart = new MimeMultipart();
		JSONArray jsonArrayObj = null;
		// DataSource source =null;
		try {
			jsonArrayObj = new JSONArray(attachmentPath);
			for (int i = 0; i < jsonArrayObj.length(); i++) {
				messageBodyPart = new MimeBodyPart();
				try {
					String attachmentPathValue = jsonArrayObj.get(i).toString();
					URL website = new URL(AWSKstorePath + attachmentPathValue);
					InputStream in = website.openStream();
					// logger.info("length:::" + in.toString().length());
					ByteArrayDataSource rawData = new ByteArrayDataSource(in, "text/plain");
					DataHandler data = new DataHandler(rawData);
					// AMAZON download
					messageBodyPart.setDataHandler(data);
					// messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(
							((jsonArrayObj.get(i).toString().substring(jsonArrayObj.get(i).toString().lastIndexOf('/')))
									.replace("/", "")).trim());
					multipart.addBodyPart(messageBodyPart);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return multipart;
	}

	public static String getUploadPath(MimeBodyPart part) {
		String outputPath = "";
		try {
			if (System.getProperty("os.name").contains("Windows")) {
				outputPath = "C:/" + "/Windows/Temp/" + part.getFileName();
			} else {
				outputPath = "/tmp/" + part.getFileName();
			}
			FileOutputStream output = new FileOutputStream(outputPath);
			InputStream input = part.getInputStream();
			byte[] buffer1 = new byte[BUFFER_SIZE];
			int byteRead;
			while ((byteRead = input.read(buffer1)) != -1) {
				output.write(buffer1, 0, byteRead);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return outputPath;
	}

	public static String getuploadPath() {
		String uploadPath = "";
		if (System.getProperty("os.name").contains("Windows")) {

			uploadPath = "C:/" + "/Windows/Temp/";
		} else {
			uploadPath = "/tmp/";
		}
		return uploadPath;
	}

	public static String stringValidation(String str) {
		StringTokenizer st = new StringTokenizer(str, " ");
		StringBuffer sb = new StringBuffer();
		while (st.hasMoreElements()) {
			sb.append(st.nextElement()).append(" ");
		}
		return sb.toString().trim();
	}

	public static String checkSpecialCharacters(String str) {
		Pattern p = Pattern.compile("[~`!@#$%^&*=+{}:><,?]");
		if (str == null) {
			System.out.println("No");
			return "no";
		}
		Matcher m = p.matcher(str);
		boolean b = m.find();
		if (b) {
			System.out.println("Yes");
			return "yes";
		} else {
			System.out.println("No");
			return "no";
		}
	}

	public static String getFileExtention(String str) {
		try {
			str = str.substring(str.lastIndexOf("."), str.length());
			return str;
		} catch (Exception e) {
			return str;
		}
	}

	public static String getFileNameWithExt(String str) {
		try {
			str = str.substring(0, str.lastIndexOf("."));
			return str;
		} catch (Exception e) {
			return str;
		}
	}

	public static MediaType contentType(String keyname) {
		String[] arr = keyname.split("\\.");
		String type = arr[arr.length - 1];
		switch (type) {
		case "txt":
			return MediaType.TEXT_PLAIN;
		case "png":
			return MediaType.IMAGE_PNG;
		case "jpg":
			return MediaType.IMAGE_JPEG;
		default:
			return MediaType.APPLICATION_OCTET_STREAM;
		}
	}

	public static String date() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String date = dateFormat.format(Calendar.getInstance().getTime());
		return date;
	}

	public static String getTextFromMessage(Message message) throws Exception {
		String result = "";
		if (message.isMimeType("text/html")) {
			Object content = message.getContent();
			if (content != null) {
				result = content.toString();
				// logger.info("Content\n " + result);
			}
		} else if (message.isMimeType("text/plain")) {
			result = message.getContent().toString();
			// logger.info("Content as Text or Html" + result);
		} else if (message.isMimeType("multipart/*")) {
			MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
			result = getTextFromMimeMultipart(mimeMultipart);
		}
		return result;
	}

	public static String getTextFromMimeMultipart(MimeMultipart mimeMultipart) throws Exception {
		String result = "";
		try {
			int count = mimeMultipart.getCount();
			for (int i = 0; i < count; i++) {
				BodyPart bodyPart = mimeMultipart.getBodyPart(i);
				if (bodyPart.getDisposition() != null)
					if (bodyPart.getContentType().contains("image") && bodyPart.getDisposition().equals("inline")) {
						BASE64DecoderStream base64DecoderStream = (BASE64DecoderStream) bodyPart.getContent();
						byte[] byteArray = IOUtils.toByteArray(base64DecoderStream);
						byte[] encodeBase64 = Base64.encodeBase64(byteArray);
						String sys = new String(encodeBase64, "UTF-8");
						String contentType = "alt=" + "\""
								+ bodyPart.getContentType().substring(bodyPart.getContentType().indexOf("name") + 5,
										bodyPart.getContentType().length()).trim();
						int value1 = result.indexOf(contentType);

						String splitvalue = result.substring(0, value1 - 1);
						int value2 = splitvalue.lastIndexOf("src=");
						String contentType1 = "data:"
								+ bodyPart.getContentType().substring(0, bodyPart.getContentType().indexOf(";")) + ";"
								+ "base64,";
						String replaceValue = result.substring(value2 + 5, value1 - 2);
						result = result.replace(replaceValue, contentType1 + sys);
					}

				if (bodyPart.isMimeType("text/html")) {
					result = result + (String) bodyPart.getContent();
				}
				// 8-10-2018 commented by jitendra to read other domain mail with .txt
				// attachment and only html data.

				if (bodyPart.getContent() instanceof MimeMultipart) {
					result = result + getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent());
				} else {
					result = "" + result;
				}
			}
			return result;
		} catch (Exception e) {
			return result;
		}
	}

	public static ArrayList<String> getAttachFiles(Message message) {
		ArrayList<String> attachmentFiles = new ArrayList<String>();
		try {
			Multipart multiPart = (Multipart) message.getContent();
			numberOfParts = multiPart.getCount();
			for (int partCount = 0; partCount < numberOfParts; partCount++) {
				MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
				if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
					// if
					// (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())||StringUtils.isBlank(part.getFileName()))
					// {
					String fileName = part.getFileName();
					attachFiles += fileName + ", ";
					File file = new File(fileName);
					String simpleFileName = file.getName();
					// logger.info("file name is" + simpleFileName);
					attachmentFiles.add(simpleFileName);
				} else {

				}
			}
			if (attachFiles.length() > 1) {
				attachFiles = attachFiles.substring(0, attachFiles.length() - 2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return attachmentFiles;
	}

	public static Timestamp getCurrentGMTdateTime() {
		Timestamp ts = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			format.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = new Date();
			SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateTime = dateParser.parse(format.format(date));
			System.out.println(dateTime);
			long gmtMilliSeconds = dateTime.getTime();
			// System.out.println(gmtMilliSeconds); // new Timestamp((gmtMilliSeconds));
			ts = new Timestamp(gmtMilliSeconds);
		} catch (Exception e) {

		}
		return ts;
	}

	public static Timestamp convertDateToSyncPostbox(String date) {
		long gmtMilliSeconds1 = 0L;
		try {
			SimpleDateFormat GMTformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date dateObj = GMTformat.parse(date); // String to Date
			String dateStr = GMTformat.format(dateObj); // Date to String
			Date dateTime = GMTformat.parse(dateStr);
			long gmtMilliSeconds = dateTime.getTime();
			gmtMilliSeconds1 = gmtMilliSeconds + 1000;
		} catch (Exception e) {
			return Timestamp.valueOf(date);
		}
		return new Timestamp(gmtMilliSeconds1);

	}

	@SuppressWarnings("deprecation")
	public static List<Timestamp> getStartAndEndDayLocalTime(Timestamp timestamp) {
		List<Timestamp> timestamps = new ArrayList<>();

		Timestamp startTime = new Timestamp(timestamp.getTime());
		startTime.setHours(0);
		startTime.setMinutes(0);
		startTime.setSeconds(0);
		startTime.setNanos(0);
		long newStartTime = startTime.getTime() - (330 * 60 * 1000);
		startTime = new Timestamp(newStartTime);
		timestamps.add(startTime);
		Timestamp endTime = new Timestamp(timestamp.getTime());
		endTime.setHours(23);
		endTime.setMinutes(59);
		endTime.setSeconds(59);
		endTime.setNanos(999999999);
		long newEndTime = endTime.getTime() - (330 * 60 * 1000);
		endTime = new Timestamp(newEndTime);
		timestamps.add(endTime);
		return timestamps;
	}
}
/*
 * if(message instanceof MimeMessage) { MimeMessage m = (MimeMessage)message;
 * Object contentObject = m.getContent(); if(contentObject instanceof Multipart)
 * { BodyPart clearTextPart = null; BodyPart htmlTextPart = null; Multipart
 * content = (Multipart)contentObject; int count = content.getCount(); for(int
 * i=0; i<count; i++) { BodyPart part = content.getBodyPart(i);
 * if(part.isMimeType("text/plain")) { clearTextPart = part; break; } else
 * if(part.isMimeType("text/html")) { htmlTextPart = part; } }
 * 
 * if(clearTextPart!=null) { result = (String) clearTextPart.getContent(); }
 * else if (htmlTextPart!=null) { String html = (String)
 * htmlTextPart.getContent(); result = Jsoup.parse(html).text(); }
 * if(result.contains("shrink-to-fit=no")) { result=
 * result.replaceFirst("shrink-to-fit=no", "shrink-to-fit=yes"); } } else if
 * (contentObject instanceof String) // a simple text message { result =
 * (String) contentObject; } else // not a mime message {
 * logger.info("notme part or multipart {0}"+message.toString()); result = null;
 * }
 * 
 * 
 * }
 */
// logger.info("result content:::: "+result);
