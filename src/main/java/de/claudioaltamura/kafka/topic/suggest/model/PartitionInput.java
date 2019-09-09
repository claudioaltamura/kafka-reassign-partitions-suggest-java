package de.claudioaltamura.kafka.topic.suggest.model;

import lombok.Data;

@Data
public class PartitionInput implements Comparable<PartitionInput> {

  private final int id;
  private final int leader;

  @Override
  public int compareTo(PartitionInput other) {
    // TODO improve
    if (this.id < other.id) return -1;
    else if (this.id == other.id) return 0;
    else return 1;
  }
}
