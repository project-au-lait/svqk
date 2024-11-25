package dev.aulait.svqk.interfaces.issue;

import dev.aulait.svqk.arch.util.BeanUtils;
import dev.aulait.svqk.domain.issue.JournalEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JournalFactory {

  public JournalEntity buildNewJournal(Integer issueId, JournalDto newJournal) {
    JournalEntity entity = BeanUtils.map(newJournal, JournalEntity.class);
    entity.setIssueId(issueId);

    return entity;
  }
}
