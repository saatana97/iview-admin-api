package cn.saatana.core.common;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class Res<T> {
	private int status;
	private String message;
	private T data;
	private Date serviceTime = new Date();

	public static <T> Res<T> ok(T data) {
		return of(HttpStatus.OK.value(), null, data);
	}

	public static <T> Res<T> ok(String message, T data) {
		return of(HttpStatus.OK.value(), message, data);
	}

	public static <T> Res<T> error(T data) {
		return of(HttpStatus.INTERNAL_SERVER_ERROR.value(), "服务器异常", data);
	}

	public static <T> Res<T> error(String message, T data) {
		return of(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, data);
	}

	public static <T> Res<T> of(int status, String message, T data) {
		return new Res<T>().setData(data).setMessage(message).setStatus(status);
	}

	private Res() {
	}

	public Date getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(Date serviceTime) {
		this.serviceTime = serviceTime;
	}

	public int getStatus() {
		return status;
	}

	public Res<T> setStatus(int status) {
		this.status = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Res<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Res<T> setData(T data) {
		this.data = data;
		return this;
	}
}
