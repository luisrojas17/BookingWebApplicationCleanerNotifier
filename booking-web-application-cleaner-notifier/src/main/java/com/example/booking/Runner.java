package com.example.booking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.example.booking.messaging.producer.AsyncPayload;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class produces a messages and put them in the queue.
 * 
 * @author josel.rojas
 *
 */
@Component
public class Runner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Runner.class);
	
	private final ObjectMapper mapper;
	private final RabbitTemplate rabbitTemplate;
	
	@Autowired
	private ConfigurableApplicationContext context;
	
	public Runner(ObjectMapper mapper, RabbitTemplate rabbitTemplate) {
		this.mapper = mapper;
		this.rabbitTemplate = rabbitTemplate;
	}
	
	@Override
	public void run(String...args) throws Exception {
		
		String message = "";
		int index = (int) ((Math.random()) * (28-1)) + 1;
		AsyncPayload payload = new AsyncPayload();
		payload.setId(index);
		payload.setModel("ROOM");
		
		message = mapper.writeValueAsString(payload);
		
		logger.info("Putting payload [{}] in queue.", message);
		
		rabbitTemplate.convertAndSend("operations", "landon.rooms.cleaner", message);
		context.close();
	}
}
