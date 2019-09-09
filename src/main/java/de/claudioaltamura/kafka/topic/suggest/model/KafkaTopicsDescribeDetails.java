package de.claudioaltamura.kafka.topic.suggest.model;

import java.util.Set;
import lombok.Data;

@Data
public class KafkaTopicsDescribeDetails {

  private final String topic;
  private final int partitionCount;
  private final int replicationFactor;
  private final Set<PartitionInput> partitions;
}
