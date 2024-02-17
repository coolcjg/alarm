package com.cjg.alarm.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.cjg.alarm.redis.RedisPublisher;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumer {
	
	Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
	
	private final RedisPublisher redisPublisher;
	
	@KafkaListener(topics="opinion")
	public void listener(Object data) {
		ConsumerRecord consumerRecord = (ConsumerRecord)data;
		String value = (String)consumerRecord.value();
		logger.info("Kafka Consumer value {}", value);
		
		redisPublisher.sendMessage(value);
	}

}
