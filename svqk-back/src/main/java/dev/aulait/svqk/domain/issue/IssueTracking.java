package dev.aulait.svqk.domain.issue;

public interface IssueTracking {

  TrackerEntity getTracker();

  IssueStatusEntity getIssueStatus();

  long getCount();
}
