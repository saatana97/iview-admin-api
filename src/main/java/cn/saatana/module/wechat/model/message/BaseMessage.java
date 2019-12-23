package cn.saatana.module.wechat.model.message;

import lombok.Data;

import java.util.Date;

@Data
public class BaseMessage {
	public static final String TYPE_EVENT = "event";
	public static final String TYPE_TEXT = "text";
	public static final String EVENT_CLICK = "CLICK";
	public static final String EVENT_VIEW = "VIEW";
	public static final String EVENT_SUBSCIBE = "subscribe";
	public static final String EVENT_UNSUBSCIBE = "unsubscribe";
	private String ToUserName;
	private String FromUserName;
	private String MsgType;
	private long CreateTime;
	private long getTime = new Date().getTime();;
	public BaseMessage(){}
	public BaseMessage(String to,String from,String type,long createTime){
		this.ToUserName = to;
		this.FromUserName = from;
		this.MsgType = type;
		this.CreateTime = createTime;
	}
	public BaseMessage reverseUser(){
		String temp = this.FromUserName;
		this.FromUserName = this.ToUserName;
		this.ToUserName = temp;
		return this;
	}
}
