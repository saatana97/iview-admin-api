package cn.saatana.module.wechat.model.message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ViewMessage extends BaseMessage {
	private String EventKey;
	public ViewMessage(){}
	public ViewMessage(String key){
		this.EventKey = key;
	}
}
