package de.claudioaltamura.kafka.topic.suggest.model;

import java.util.Set;
import lombok.Data;

@Data
public class KafkaReassignPartitions {

  private int version = 1;
  private final Set<Partition> partitions;
}
