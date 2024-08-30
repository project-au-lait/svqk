import type { IssueModel, IssueStatusModel } from '../api/Api';
import StringUtils from '../arch/StringUtils';

export default class IssueFormFactory {
  static createNewForm() {
    const issue = { issueStatus: {} as IssueStatusModel } as IssueModel;
    return issue;
  }

  static createRandomForm() {
    const issue = this.createNewForm();
    issue.subject = StringUtils.generateRandomString();
    issue.description = `${issue.subject}の説明`;
    return issue;
  }
}
