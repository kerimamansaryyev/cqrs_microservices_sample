package com.example.productservice.integration;
import com.example.productservice.util.AMQMessageMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
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
