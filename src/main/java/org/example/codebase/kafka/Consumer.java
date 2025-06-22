package org.example.codebase.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Consumer {

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listener(String message) {
        log.info(message);
    }

    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void listenerV2(ConsumerRecord<String, String> message, Acknowledgment acknowledgment) {
        var value = message.value();
        var topic = message.topic();
        var partition = message.partition();
        var offset = message.offset();
        var key = message.key();

        log.info(value + "-" + topic + "-" + partition + "-" + offset + "-" + key);
        acknowledgment.acknowledge();
    }
}
