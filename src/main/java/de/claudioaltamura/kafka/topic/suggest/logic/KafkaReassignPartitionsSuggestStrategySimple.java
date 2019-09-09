package de.claudioaltamura.kafka.topic.suggest.logic;

import java.util.Set;
import java.util.TreeSet;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaReassignPartitions;
import de.claudioaltamura.kafka.topic.suggest.model.KafkaTopicsDescribeDetails;
import de.claudioaltamura.kafka.topic.suggest.model.Partition;
import de.claudioaltamura.kafka.topic.suggest.model.PartitionInput;

public class KafkaReassignPartitionsSuggestStrategySimple
    implements KafkaReassignPartitionsSuggestStrategy {

  private int lastIndex = 0;
  private int[] brokerCounter;

  @Override
  public KafkaReassignPartitions suggest(String topic,
      KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails, int[] brokerIds) {
    // TODO input validation
    brokerCounter = new int[brokerIds.length];

    Set<Partition> partitions = createPartitionWithLeader(topic, kafkaTopicsDescribeDetails);

    fillPartitionsWithFollowReplicas(kafkaTopicsDescribeDetails, partitions, brokerIds);

    KafkaReassignPartitions kafkaReassignmentPartitions = new KafkaReassignPartitions(partitions);

    assertEqualDistribution(kafkaTopicsDescribeDetails, brokerIds);

    return kafkaReassignmentPartitions;
  }

  private void assertEqualDistribution(KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails,
      int[] brokerIds) {
    int numberOfReplicas = kafkaTopicsDescribeDetails.getPartitionCount()
        * kafkaTopicsDescribeDetails.getReplicationFactor();
    int avgReplicasPerBroker = numberOfReplicas / brokerIds.length;
    for (int i = 0; i < brokerCounter.length; i++) {
      if (brokerCounter[i] != avgReplicasPerBroker) {
        throw new IllegalStateException(String.format("broker '%d' has more replicas '%d' than average '%d'.", i, brokerCounter[i], avgReplicasPerBroker));
      }
    }
  }

  private Set<Partition> createPartitionWithLeader(String topic,
      KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails) {
    Set<Partition> partitions = new TreeSet<>();
    for (PartitionInput partitionInput : kafkaTopicsDescribeDetails.getPartitions()) {
      int[] replicas = new int[] {partitionInput.getLeader(), 0, 0};
      partitions.add(new Partition(topic, partitionInput.getId(), replicas));
      increaseBrokerCounterFor(partitionInput.getLeader());
    }
    return partitions;
  }

  private void increaseBrokerCounterFor(int broker) {
    brokerCounter[broker] = brokerCounter[broker] + 1;
  }

  private void fillPartitionsWithFollowReplicas(
      KafkaTopicsDescribeDetails kafkaTopicsDescribeDetails, Set<Partition> partitions,
      int[] brokerIds) {
    for (int i = 1; i < kafkaTopicsDescribeDetails.getReplicationFactor(); i++) {
      for (Partition partition : partitions) {
        int[] replicas = partition.getReplicas();
        int brokerPredecessorReplica = replicas[i - 1];
        int brokerCurrentReplica = determineNextBroker(brokerPredecessorReplica, brokerIds);
        replicas[i] = brokerCurrentReplica;
        increaseBrokerCounterFor(brokerCurrentReplica);
      }
    }
  }

  private int determineNextBroker(int brokerPredecessorReplica, int[] brokerIds) {
    int index = increaseLastIndex(brokerIds);

    int brokerCurrentReplica = nextBroker(index, brokerIds);
    while (brokerCurrentReplica == brokerPredecessorReplica) {
      index = increaseLastIndex(brokerIds);
      brokerCurrentReplica = brokerIds[index];
    }

    return brokerCurrentReplica;
  }

  private int increaseLastIndex(int[] brokerIds) {
    int index = lastIndex++;
    if (lastIndex == brokerIds.length) {
      lastIndex = 0;
    }
    return index;
  }

  private int nextBroker(int index, int[] brokerIds) {
    int minIndex = findBrokerWithLowestPartitionCount();
    return brokerIds[minIndex];
  }

  public int findBrokerWithLowestPartitionCount() {
    int minValue = brokerCounter[0];
    int minIndex = 0;
    for (int i = 1; i < brokerCounter.length; i++) {
      if (brokerCounter[i] < minValue) {
        minValue = brokerCounter[i];
        minIndex = i;
      }
    }
    return minIndex;
  }

}
