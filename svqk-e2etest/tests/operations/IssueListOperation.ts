import type { Page } from '@playwright/test';
import IssueListPage from '../pages/IssueListPage';
import { DryRun } from '../arch/DryRun';

export default class IssueListOperation {
  private issueListPage: IssueListPage;
  constructor(page: Page, dryRun: DryRun) {
    this.issueListPage = new IssueListPage(page, dryRun);
  }

  async gotoIssueList() {
    await this.issueListPage.gotoIssueList();
  }

  async gotoIssue(subject: string) {
    await this.issueListPage.clickIssueNoLinkBySubject(subject);
  }

  async searchIssue(subject: string) {
    await this.issueListPage.inputSearch(subject);
    await this.issueListPage.clickSearchBtn();
    await this.issueListPage.expectSearchResult(subject);
  }
}
