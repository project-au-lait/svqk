import IssueInputPage from '../pages/issue-input/IssueInputPage';
import type { IssueModel } from '../api/Api';
import BaseFacade from '../arch/BaseFacade';
import MenuBar from '../pages/menu-bar/MenuBar';

export class IssueFacade extends BaseFacade {
  async createIssue(menuBar: MenuBar, issue: IssueModel) {
    this.logStart('Issue Registration');
    const issueListPage = await menuBar.gotoIssueListPage();
    const issueInputPage = await issueListPage.gotoNewIssuePage();
    await issueInputPage.save(issue);
    return issueInputPage;
  }

  async referenceIssueBySubject(menuBar: MenuBar, subject: string) {
    this.logStart('Issue Reference');
    const issueListPage = await menuBar.gotoIssueListPage();
    await issueListPage.searchIssueBySubject(subject);
    const issueInputPage = await issueListPage.gotoIssueBySubject(subject);

    return issueInputPage;
  }

  async updateIssue(issueInputPage: IssueInputPage, issue: IssueModel) {
    this.logStart('Issue Update');
    await issueInputPage.save(issue);
  }

  async expectIssue(issueInputPage: IssueInputPage, issue: IssueModel) {
    await issueInputPage.expectIssue(issue);
  }
}
