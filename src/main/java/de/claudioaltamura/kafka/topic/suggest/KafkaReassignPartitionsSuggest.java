package de.claudioaltamura.kafka.topic.suggest;

import de.claudioaltamura.kafka.topic.suggest.logic.KafkaReassignPartitionsSuggestController;
import de.claudioaltamura.kafka.topic.suggest.logic.KafkaReassignPartitionsWriter;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaReassignPartitions;
import java.io.IOException;

public class KafkaReassignPartitionsSuggest {

  public static void main(String[] args) throws IOException {
    var topic = "__consumer_offsets";
    var paritionsCount = 50;
    var replicationFactor = 3;
    int[] partitionLeader = {
      2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2,
      3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3, 0, 2, 3
    };
    int[] brokerIds = {0, 1, 2, 3, 4};

    KafkaReassignPartitionsSuggestController controller =
        new KafkaReassignPartitionsSuggestController();
    KafkaReassignPartitions kafkaReassignPartitions =
        controller.process(topic, paritionsCount, replicationFactor, partitionLeader, brokerIds);
    KafkaReassignPartitionsWriter writer = new KafkaReassignPartitionsWriter();
    System.out.println(writer.write(kafkaReassignPartitions));
  }
}
