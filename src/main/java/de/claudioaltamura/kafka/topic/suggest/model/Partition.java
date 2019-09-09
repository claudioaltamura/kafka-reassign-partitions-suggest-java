package de.claudioaltamura.kafka.topic.suggest.model;

import lombok.Data;

@Data
public class Partition implements Comparable<Partition> {

  private final String topic;
  private final int partition;
  private final int[] replicas;

  @Override
  public int compareTo(Partition other) {
    // TODO improve
    if (other.getTopic() == null) return 1;
    var topicComparison = topic.compareTo(other.topic);
    if (topicComparison != 0) {
      return topicComparison;
    }

    if (this.partition < other.partition) return -1;
    else if (this.partition == other.partition) return 0;
    else return 1;
  }
}
