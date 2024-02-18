package com.cjg.alarm.sse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class SseService {
	
	public static Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
	
	Logger logger = LoggerFactory.getLogger(SseService.class);
	
	public void saveEmitter(String userId, SseEmitter emitter) {
		sseEmitters.put(userId, emitter);
	}
	
	public void removeEmitter(String userId) {
		sseEmitters.remove(userId);
	}
	
	public void send(String userId, String message) {
		SseEmitter emitter = sseEmitters.get(userId);
		
		try {
			emitter.send(SseEmitter.event().name("alarm").data(message));   
		}catch(Exception e) {
			logger.error(e.toString());
			sseEmitters.remove(userId);
		}
		
	}

}
