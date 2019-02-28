package cn.saatana.core.auth.entity;

public class AuthorizationInformation {
	private String sessionId;
	private String token;
	private Authorizer auth;

	public AuthorizationInformation() {
	}

	public AuthorizationInformation(String token, String sessionId, Authorizer auth) {
		this.token = token;
		this.sessionId = sessionId;
		this.auth = auth;
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

	public Authorizer getAuth() {
		return auth;
	}

	public void setAuth(Authorizer auth) {
		this.auth = auth;
	}

	@Override
	public String toString() {
		return "sessionId=" + sessionId + ", token=" + token + ", auth=" + auth.getUsername();
	}

}
