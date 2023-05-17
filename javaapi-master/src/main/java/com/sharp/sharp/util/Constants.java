/**
 * 
 */
package com.sharp.sharp.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author Elavarasan
 *
 */
public interface Constants {
	// Type of Lettets
	static final int LETTER_TYPE_NEW = 0;
	static final int LETTER_TYPE_REPLY = 1;
	static final int LETTER_TYPE_FORWARD = 2;
	static final int LETTER_TYPE_REMINDER = 3;

	// Priority of Letters
	static final int LETTER_PRIORITY_HIGH = 1;
	static final int LETTER_PRIORITY_MEDIUM = 2;
	static final int LETTER_PRIORITY_LOW = 3;

	// Letter Receiver Type
	static final int RECEIVER_TYPE_TO = 1;
	static final int RECEIVER_TYPE_COPY = 2;
	static final int RECEIVER_TYPE_CONFIDENTIAL = 3;

	// Attachment
	static final int ATTACHMENT_TYPE_DOCUMENT = 1;
	static final int ATTACHMENT_TYPE_PDF = 2;
	static final int ATTACHMENT_TYPE_IMG = 3;

	// Unopened Letter Landing Page
	static final String UNOPENED_GROUPBY_INBOX = "inbox"; // Personal Folder, Group and Multi User Fodlers
	static final String UNOPENED_GROUPBY_GROUP = "Group Inbox";
	static final String UNOPENED_GROUPBY_MULTIUSER = "Multi User Inbox";

	// Katchup Message Status
	static final int MESSAGE_STATUS_SENT = 0;
	static final int MESSAGE_STATUS_UNOPENED = 1;
	static final int MESSAGE_STATUS_OPENED = 2;
	static final int MESSAGE_STATUS_NOTSENT = 3;
	static final int MESSAGE_STATUS_GROUPINFO = 4;

	static final String MESSAGE_STATUS_DESC_SENT = "Sent";
	static final String MESSAGE_STATUS_DESC_UNOPENED = "Unread";
	static final String MESSAGE_STATUS_DESC_OPENED = "Read";
	static final String MESSAGE_STATUS_DESC_NOTSENT = "Not Sent";
	static final String MESSAGE_STATUS_DESC_GROUPINFO = "Group Read Status";
	// added by jitendra
	static final String STATUS = "status";
	static final String VALUE = "value";
	static final String URLPATH = "urlPath";
	static final String ERROR = "error";
	static final String ERROR_CODE = "errorCode";
	static final String ERROR_VALUE = "errorValue";
	static final String ERROR_MSG = "errorMessage";

	// added by jitendra ends..
	static final String SUCCESS = "SUCCESS";
	static final String FAILURE = "FAILURE";
	static final String NOTAPPLICABLE = "Not Applicable";

	// Type of messages
	static final int MESSAGE_TYPE_IM = 0;
	static final int MESSAGE_TYPE_REPLY = 1;
	static final int MESSAGE_TYPE_FORWARD = 2;
	static final int MESSAGE_TYPE_REMINDER = 3;
	static final int MESSAGE_TYPE_SMS = 4;
	static final int MESSAGE_TYPE_EDIT = 5;
	static final int MESSAGE_TYPE_NOTE = 6;
	static final int MESSAGE_TYPE_RECALL = 7;
	static final int MESSAGE_TYPE_COMMENT = 8;
	static final int MESSAGE_TYPE_CLARIFY = 9;
	static final int MESSAGE_TYPE_INVITEDCONTACT = 10;

	// Katchup Hardcoded String Value
	static final String MESSAGE_REPLY_VALUE = "KPOST_OLD_NEW_MSG_REPLY";
	static final String MESSAGE_REMINDER_VALUE = "KPOST_OLD_NEW_MSG_REMINDER";
	static final String MESSAGE_FORWARD_VALUE = "KPOST_OLD_NEW_MSG_FORWARD";

	static final String READ_FLAG_NO = "N";
	static final String READ_FLAG_YES = "Y";
	
	static final char MAIL_DELIVERY_SUCCESS = 'Y';
	static final char MAIL_DELIVERY_FAIL = 'N';

	static final String READLETTER_DATE = "E MMM dd HH:mm:ss Z yyyy";

	String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	// Status of Letters
	static final int SENTLETTERS_NOT_OPENED = 0;
	static final int REPLY_NOT_RECEIVED = 1;
	static final int REPLY_NOT_SENT = 2;
	static final char DELETEDBY_YES = 'Y';
	static final char DELETEDBY_NO = 'N';
	static final int MAX_POSTS = 15;
	static final int MAX_KATCHUP_MESSAGES = 25;
	static final String LETTER_NOTIFICATION = "You got new mail in your postbox";// changed letter to mail by jitendra

	static final String HIDE_MOBILE = "*****";
	static final String HIDE_DOB = "*****";
	// For saving contact types in personal directory
	static final int PHONE_CONTACT = 0;
	static final int OTHER_DOMAIN_CONTACT = 1;
	static final int MIGRATED_CONTACT = 2;
	// For OTP message
	static final String OTP_MESSAGE = "Your OTP to import contacts is ";
	// INVITE MESSAGE
	static final String INVITE_MESSAGE = "KPOST Invitation: I am glad to inform you about the most advanced communication system called KPOST that I am using now. You can also join.";
	static final String REINVITE_MESSAGE = "KPOST Reminder: This is in connection with my earlier invitation to join KPOST. Kindly join immediately.";
	static final String START_VALUE = "0";
	// Changed and Added by Jitendra
	static final String INVITATION_SUBJECT="Joined Recently in KPOST";
	static final String INVITED_NOTIFICATION_MSG = "I have joined KPOST - a comprehensive Integrated Communication Platform. Now on, we can communicate easily and efficiently. \n\nThanks for Inviting me";
	static final String AUTOMATIC_NOTIFICATION_MSG = "I have joined KPOST - a comprehensive Integrated Communication Platform. Now on, we can communicate easily and efficiently.";
	
	static final String NON_INVITED_NOTIFICATION_MSG = "I have joined KPOST - a comprehensive Integrated Communication Platform. Now on, we can communicate easily and efficiently";
	static final String RESOLVED_USER_QUERIES = "Admin Response";
	// For save message filtertypes
	static final String FILTERTYPE_MSGTYPE = "messagetype";
	static final String FILTERTYPE_MSG_WITH_ATTACHMENT = "withattachment";
	static final String FILTERTYPE_MSG_RECEIVED_FWD = "receivedfwd";
	static final String FILTERTYPE_MSG_WITHOUT_SUB = "withoutsubject";
	static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";

	List<String> videoFormat = Arrays.asList(".3gp", ".mp4", ".avi", ".ogg", ".wmv", ".webm", ".flv", ".hdv", ".mxf");
	List<String> imageFormat = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".tiff");
	List<String> audioFormat = Arrays.asList(".mp3", ".acc", ".he-aac", ".ac3", ".eac3", ".vorbis", ".wma", ".pcm",
			".amr");

}
