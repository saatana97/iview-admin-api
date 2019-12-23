package cn.saatana.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpUtils {
	private static final Logger log = Logger.getLogger(HttpUtils.class.getName());
	private static final ObjectMapper JSON = new ObjectMapper();

	public static <T> T post(String url, Map<String, ?> params, Map<String, ?> body, Class<T> responseClass) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (params == null) {
			params = new HashMap<>();
		}
		T response = client.postForObject(url, body, responseClass, params);
		try {
			log.info("==========================================================================");
			log.info("url:\t" + url);
			log.info("params:\t" + JSON.writer().writeValueAsString(params));
			log.info("response:\t" + JSON.writer().writeValueAsString(response));
			log.info("==========================================================================");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.warning(e.getMessage());
		}
		return response;
	}

	public static <T> T get(String url, Map<String, ?> params, Class<T> responseClass) {
		RestTemplate client = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> map = new HashMap<>();
		if (params != null) {
			params.forEach((key, value) -> {
				if (value instanceof String) {
					map.put((String) key, (String) value);
				} else {
					try {
						map.put((String) key, JSON.writer().writeValueAsString(value));
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						log.warning(e.getMessage());
					}
				}
			});
		}
		T response = client.getForObject(url, responseClass, map);
		try {
			log.info("==========================================================================");
			log.info("url:\t" + url);
			log.info("params:\t" + JSON.writer().writeValueAsString(params));
			log.info("response:\t" + JSON.writer().writeValueAsString(response));
			log.info("==========================================================================");
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			log.warning(e.getMessage());
		}
		return response;
	}
}
