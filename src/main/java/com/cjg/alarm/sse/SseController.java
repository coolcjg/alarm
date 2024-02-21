package com.cjg.alarm.sse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/sse")
@RequiredArgsConstructor
public class SseController {
	
	Logger logger = LoggerFactory.getLogger(SseController.class);
	private final SseService sseService;
	
	@GetMapping(value = "", consumes = MediaType.ALL_VALUE)
	public SseEmitter streamSseMvc(@RequestParam(value="userId") String userId) {
		
		//현재 클라이언트를 위한 SssEmitter 생성
		SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
		
		sseService.saveEmitter(userId, emitter);
		
		Gson gson = new Gson();
		
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("type", "접속");
		jsonObject.addProperty("message", "hello world");
		
		String jsonStr = gson.toJson(jsonObject);
		
		sseService.send(userId, jsonStr);
		
		emitter.onCompletion(() -> sseService.removeEmitter(userId));
		emitter.onTimeout(()->sseService.removeEmitter(userId));
		emitter.onError((e) -> sseService.removeEmitter(userId));
		
		return emitter;
	}
	
}
