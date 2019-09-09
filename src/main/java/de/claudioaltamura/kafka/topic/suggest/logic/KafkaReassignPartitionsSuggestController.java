package de.claudioaltamura.kafka.topic.suggest.logic;

import de.claudioaltamura.kafka.topic.suggest.model.KafkaReassignPartitions;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaTopicsDescribeDetails;

public class KafkaReassignPartitionsSuggestController {

  public KafkaReassignPartitions process(
      String topic,
      int paritionsCount,
      int replicationFactor,
      int[] partitionLeader,
      int[] brokerIds) {
    // TODO input validation
    KafkaReassignPartitionsReader reader = new KafkaReassignPartitionsReader();
    KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails =
        reader.read(topic, paritionsCount, replicationFactor, partitionLeader);
    KafkaReassignPartitionsSuggestStrategy partitionsSuggestStrategy =
        new KafkaReassignPartitionsSuggestStrategySimple();
    KafkaReassignPartitions kafkaReassignPartitions =
        partitionsSuggestStrategy.suggest(topic, kafkaTopicsDescribeDetails, brokerIds);
    return kafkaReassignPartitions;
  }
}
