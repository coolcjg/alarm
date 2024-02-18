package com.cjg.alarm.sse;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
		sseService.send(userId, "hello world");
		
		emitter.onCompletion(() -> sseService.removeEmitter(userId));
		emitter.onTimeout(()->sseService.removeEmitter(userId));
		emitter.onError((e) -> sseService.removeEmitter(userId));
		
		return emitter;
	}
	
}
