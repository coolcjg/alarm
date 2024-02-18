package com.cjg.alarm.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cjg.alarm.sse.SseService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisSubscriber implements MessageListener {
	
	Logger logger = LoggerFactory.getLogger(RedisSubscriber.class);
	private final RedisTemplate<String, Object> redisTemplate;
	private final SseService sseService;
	
	@Override
	public void onMessage(Message message, byte[] pattern) {
		String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
		logger.info("redis sub message {}", publishMessage);
		
		JsonObject jo = JsonParser.parseString(publishMessage).getAsJsonObject();	
		String userId = jo.get("userId").getAsString();
		
		sseService.send(userId, publishMessage);
	}

}
