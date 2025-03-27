package com.example.demo.consumers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.demo.services.StockService;

@Component
public class KafkaConsumer {

    private final static Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private List<String> messages = Collections.synchronizedList(new ArrayList<>());

    @Autowired
    private StockService stockService;

    @KafkaListener(topics = "my-first-topic", groupId = "my-first-group")
    public void consume(ConsumerRecord<String, String> record) {
        String message = record.value();
        LOGGER.info("Message consommé : {}", message);

        messages.add(message);

        parseAndUpdateStock(message);
    }

    private void parseAndUpdateStock(String message) {
        try {
            String[] parts = message.split(" : ");
            if (parts.length == 2) {
                String produit = parts[0].trim();
                int quantite = Integer.parseInt(parts[1].trim());

                stockService.getStock().addStock(produit, -quantite);
                LOGGER.info("Stock mis à jour : {} -> {}", produit,stockService.getStock().getStock().get(produit) );
            } else {
                LOGGER.warn("Format de message invalide : {}", message);
            }
        } catch (Exception e) {
            LOGGER.error("Erreur lors du parsing du message : {}", message, e);
        }
    }

    public List<String> getMessages() {
        return messages;
    }
}
