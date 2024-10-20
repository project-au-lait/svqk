package dev.aulait.svqk.interfaces.issue;

import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import lombok.Data;
import lombok.Getter;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
public class IssueTrackingDto {

  @Schema(required = true)
  private SortedSet<TrackerCountDto> trackers = new TreeSet<>();

  @Schema(required = true)
  private SortedSet<IssueStatusDto> issueStatuses = new TreeSet<>();

  @Data
  public static class TrackerCountDto implements Comparable<TrackerCountDto> {
    @Schema(required = true)
    private TrackerDto tracker;

    @Schema(required = true)
    private SortedMap<String, IssueStatusCountDto> issueStatusMap = new TreeMap<>();

    @Getter(lazy = true)
    @Schema(required = true, implementation = Integer.class)
    private final int total =
        issueStatusMap.values().stream().mapToInt(IssueStatusCountDto::getCount).sum();

    @Override
    public int compareTo(TrackerCountDto o) {
      return tracker.getId().compareTo(o.getTracker().getId());
    }
  }

  @Data
  public static class IssueStatusCountDto {
    @Schema(required = true)
    private IssueStatusDto issueStatus;

    @Schema(required = true)
    private int count;
  }
}
