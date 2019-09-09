package de.claudioaltamura.kafka.topic.suggest.logic;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaReassignPartitions;
import java.util.Objects;

public class KafkaReassignPartitionsWriter {

  private ObjectMapper objectMapper = new ObjectMapper();

  public String write(KafkaReassignPartitions kafkaReassignmentPartitions)
      throws JsonProcessingException {
    // TODO input validation
    Objects.requireNonNull(kafkaReassignmentPartitions);

    return objectMapper
        .writerWithDefaultPrettyPrinter()
        .writeValueAsString(kafkaReassignmentPartitions);
  }
}
