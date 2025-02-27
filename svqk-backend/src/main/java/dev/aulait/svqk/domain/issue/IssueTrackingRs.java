package dev.aulait.svqk.domain.issue;

public interface IssueTrackingRs {

  TrackerEntity getTracker();

  IssueStatusEntity getIssueStatus();

  long getCount();
}
