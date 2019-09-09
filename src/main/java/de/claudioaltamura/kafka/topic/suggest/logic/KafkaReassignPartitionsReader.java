package de.claudioaltamura.kafka.topic.suggest.logic;

import de.claudioaltamura.kafka.topic.suggest.model.KafkaTopicsDescribeDetails;
import de.claudioaltamura.kafka.topic.suggest.model.PartitionInput;
import java.util.Set;
import java.util.TreeSet;

public class KafkaReassignPartitionsReader {

  public KafkaTopicsDescribeDetails read(
      String topic, int paritionsCount, int replicationFactor, int[] partitionLeader) {
    // TODO input validation
    assertPartitionCountEqualsPartitionLeaders(paritionsCount, partitionLeader);
    Set<PartitionInput> partitionsInput = new TreeSet<>();
    for (int i = 0; i < paritionsCount; i++)
      partitionsInput.add(new PartitionInput(i, partitionLeader[i]));

    KafkaTopicsDescribeDetails details =
        new KafkaTopicsDescribeDetails(topic, paritionsCount, replicationFactor, partitionsInput);

    return details;
  }

  private void assertPartitionCountEqualsPartitionLeaders(
      int paritionsCount, int[] partitionLeader) {

    if (partitionLeader.length != paritionsCount) {
      var errorMsg =
          String.format(
              "partition leader length '%d' must be equal to partitions count '%d'.",
              partitionLeader.length, paritionsCount);
      throw new IllegalArgumentException(errorMsg);
    }
  }
}
