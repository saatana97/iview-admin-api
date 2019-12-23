package cn.saatana.module.wechat.model.message;

import cn.saatana.core.utils.XmlUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TextMessage extends BaseMessage {
	private String Content;
	private String MsgId;
	public TextMessage(){}
	public TextMessage(Map<String,String> map){
		this(map.get("ToUserName"),map.get("FromUserName"),map.get("Content"),map.get("MsgId"),Long.parseLong(map.get("CreateTime")));
	}
	public TextMessage(String to,String from,String content,String msgId,long createTime){
		super(to,from,BaseMessage.TYPE_TEXT,createTime);
		this.Content = content;
		this.MsgId = msgId;
	}
	@Override
	public String toString() {
		return XmlUtils.textMsgToxml(this);
	}
}
