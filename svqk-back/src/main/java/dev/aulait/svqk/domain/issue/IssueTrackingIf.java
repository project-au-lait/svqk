package dev.aulait.svqk.domain.issue;

public interface IssueTrackingIf {

  TrackerEntity getTracker();

  IssueStatusEntity getIssueStatus();

  long getCount();
}
