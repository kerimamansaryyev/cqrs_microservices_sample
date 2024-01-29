package com.example.stockservice.integration;

import com.example.stockservice.util.AMQMessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AMQMessageSender {

    private final JmsTemplate jmsTemplate;

    public <T> void sendMessage(T message, String channel)  {
        try {
            jmsTemplate.convertAndSend(channel, AMQMessageMapper.asString(message));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
