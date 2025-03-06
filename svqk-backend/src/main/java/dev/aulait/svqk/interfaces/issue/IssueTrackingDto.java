package dev.aulait.svqk.interfaces.issue;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import lombok.Data;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueTrackingDto {

  // {key: trackerId, value: {key: statusId, value: count}} //NOSONAR
  @Schema(required = true)
  private Map<String, Map<String, Long>> trackerStatusCountMap = new HashMap<>();

  // {key: trackerId, value: sum of count} //NOSONAR
  @Schema(required = true)
  private Map<String, Long> trackerCountMap = new HashMap<>();

  public void add(String trackerId, String statusId, long count) {
    trackerStatusCountMap.computeIfAbsent(trackerId, k -> new TreeMap<>()).put(statusId, count);
  }

  public void add(String trackerId, long count) {
    long sum = trackerCountMap.computeIfAbsent(trackerId, k -> 0L) + count;
    trackerCountMap.put(trackerId, sum);
  }
}
