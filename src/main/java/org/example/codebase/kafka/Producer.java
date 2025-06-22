package org.example.codebase.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Producer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendV1() {
        kafkaTemplate.send("my-topic", "hello");
    }

    public void sendV2() {
        ProducerRecord<String, String> consumerRecord = new ProducerRecord<>("my-topic", "hello");
        kafkaTemplate.send(consumerRecord);
    }
}
