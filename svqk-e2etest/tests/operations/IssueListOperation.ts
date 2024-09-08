import IssueListPage from '../pages/IssueListPage';
import IssueInputOperation from './IssueInputOperation';
import BasePage from '../arch/BasePage';

export default class IssueListOperation {
  private issueListPage: IssueListPage;

  constructor(page: BasePage) {
    this.issueListPage = new IssueListPage(page);
  }

  async gotoNewIssuePage() {
    await this.issueListPage.clickNewIssueLink();
    return new IssueInputOperation(this.issueListPage);
  }

  async gotoIssueBySubject(subject: string) {
    await this.issueListPage.clickIssueNoLinkBySubject(subject);
    return new IssueInputOperation(this.issueListPage);
  }

  async searchIssueBySubject(subject: string) {
    await this.issueListPage.inputSearch(subject);
    await this.issueListPage.clickSearchBtn();
    await this.issueListPage.expectSearchResult(subject);
  }
}
