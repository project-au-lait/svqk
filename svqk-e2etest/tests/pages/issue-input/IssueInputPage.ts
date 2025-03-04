import IssueInputPageElement from './IssueInputPageElement';
import type { IssueModel } from '../../api/Api';
import BasePageElement from '../../arch/BasePageElement';

export default class IssueInputPage {
  private issueInputPageEl: IssueInputPageElement;

  constructor(page: BasePageElement) {
    this.issueInputPageEl = new IssueInputPageElement(page);
  }

  async save(issue: IssueModel) {
    await this.issueInputPageEl.inputSubject(issue.subject);
    await this.issueInputPageEl.inputDescription(issue.description!);

    await this.issueInputPageEl.clickSaveBtn();
  }

  async expectSavedSuccessfully() {
    await this.issueInputPageEl.expectSavedSuccessfully();
  }

  async expectIssue(issue: IssueModel) {
    await this.issueInputPageEl.expectSubject(issue.subject);
    await this.issueInputPageEl.expectDescription(issue.description);
  }

  async delete() {
    await this.issueInputPageEl.clickDeleteBtn();
  }

  async expectDeletedSuccessfully() {
    await this.issueInputPageEl.expectDeletedSuccessfully();
  }
}
