package cn.saatana.core.entity;

public class UserInfo {
	private String sessionId;
	private String token;
	private User user;

	public UserInfo(String token, String sessionId, User user) {
		this.token = token;
		this.sessionId = sessionId;
		this.user = user;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "sessionId=" + sessionId + ", token=" + token + ", user=" + user.getUsername();
	}

}
