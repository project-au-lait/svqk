import IssueInputPage from '../pages/IssueInputPage';
import type { IssueModel } from '../api/Api';
import BasePage from '../arch/BasePage';

export default class IssueInputOperation {
  private issueInputPage: IssueInputPage;

  constructor(page: BasePage) {
    this.issueInputPage = new IssueInputPage(page);
  }

  async save(issue: IssueModel) {
    await this.issueInputPage.inputSubject(issue.subject);
    await this.issueInputPage.inputDescription(issue.description!);

    await this.issueInputPage.clickSaveBtn();
  }

  async expectIssue(issue: IssueModel) {
    await this.issueInputPage.expectSubject(issue.subject);
    await this.issueInputPage.expectDescription(issue.description);
  }
}
