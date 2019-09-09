package de.claudioaltamura.kafka.topic.suggest.logic;

import de.claudioaltamura.kafka.topic.suggest.model.KafkaReassignPartitions;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaTopicsDescribeDetails;

public interface KafkaReassignPartitionsSuggestStrategy {

  KafkaReassignPartitions suggest(
      String topic, KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails, int[] brokerIds);
}
