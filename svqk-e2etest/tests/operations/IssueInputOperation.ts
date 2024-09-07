import { Page } from '@playwright/test';
import IssueInputPage from '../pages/IssueInputPage';
import type { IssueModel } from '../api/Api';
import { DryRun } from '../arch/DryRun';

export default class IssueInputOperation {
  private issueInputPage: IssueInputPage;

  constructor(page: Page, dryRun: DryRun) {
    this.issueInputPage = new IssueInputPage(page, dryRun);
  }

  async gotoNewIssue() {
    await this.issueInputPage.clickNewIssueLink();
  }

  async save(issue: IssueModel) {
    await this.issueInputPage.inputSubject(issue.subject);
    await this.issueInputPage.inputDescription(issue.description!);

    await this.issueInputPage.clickSaveBtn();
  }

  async expectIssue(issue: IssueModel) {
    await this.issueInputPage.expectIssueDetail(issue);
  }
}
