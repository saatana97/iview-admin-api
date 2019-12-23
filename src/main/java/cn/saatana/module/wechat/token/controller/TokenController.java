package cn.saatana.module.wechat.token.controller;

import cn.saatana.config.AppProperties;
import cn.saatana.core.annotation.Guest;
import cn.saatana.module.system.user.entity.User;
import cn.saatana.module.system.user.service.UserService;
import cn.saatana.core.utils.SHAUtils;
import cn.saatana.core.utils.XmlUtils;
import cn.saatana.module.wechat.model.message.BaseMessage;
import cn.saatana.module.wechat.model.message.TextMessage;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Logger;

@Guest
@RestController
@RequestMapping("/wechat")
public class TokenController {
	private static final Logger log = Logger.getLogger(TokenController.class.getName());
	@Autowired
	private AppProperties app;
	@Autowired
	private UserService authService;

	@RequestMapping("token")
	public String index(String signature, String timestamp, String nonce, String echostr, String openid,
			HttpServletRequest request) throws NoSuchAlgorithmException, IOException, DocumentException {
		String res = "";
		if (echostr != null) {
			String[] arr = { app.getToken(), timestamp, nonce };
			Arrays.sort(arr);
			if (new String(SHAUtils.encode(arr[0] + arr[1] + arr[2])).equals(signature)) {
				res = echostr;
			}
		} else {
			Map<String, String> map = XmlUtils.xmlToMap(request);
			String msgType = map.get("MsgType");
			TextMessage textMessage = new TextMessage(map);
			User auth = authService.getByOpenId(openid);
			switch (msgType) {
			case BaseMessage.TYPE_TEXT:
				log.info("收到用户消息：" + textMessage.getContent());
				textMessage.reverseUser();
				textMessage.setContent(textMessage.getContent().replace("向文可", "陈瑶"));
				res = textMessage.toString();
				break;
			case BaseMessage.TYPE_EVENT:
				String event = map.get("Event");
				switch (event) {
				case BaseMessage.EVENT_SUBSCIBE:
					textMessage.setContent("来了老弟");
					textMessage.reverseUser();
					res = textMessage.toString();
					log.info("用户关注：" + map.get("FromUserName"));

					if (auth == null) {
						auth = new User(openid);
						authService.create(auth);
					} else {
						authService.restore(auth);
					}
					break;
				case BaseMessage.EVENT_UNSUBSCIBE:
					textMessage.setContent("拜拜了您嘞");
					textMessage.reverseUser();
					res = textMessage.toString();
					log.info("取消关注：" + map.get("FromUserName"));

					if (auth != null) {
						authService.remove(auth);
					}
					break;
				case BaseMessage.EVENT_VIEW:
					// Safer.login(auth);
					break;
				}
				break;
			}
		}
		log.info("系统自动回复：" + res);
		return res;
	}
}
