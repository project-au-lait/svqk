import type { IssueModel, IssueStatusModel } from '@api/Api';
import StringUtils from '@arch/StringUtils';

export default class IssueFactory {
  static create() {
    const issue = { issueStatus: {} as IssueStatusModel } as IssueModel;
    return issue;
  }

  // <.>
  static createRandomIssue() {
    const issue = this.create();
    issue.subject = StringUtils.generateRandomString();
    issue.description = `${issue.subject}の説明`;
    return issue;
  }
}
