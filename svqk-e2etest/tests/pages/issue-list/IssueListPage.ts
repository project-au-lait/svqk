import IssueListPageElement from './IssueListPageElement';
import IssueInputPage from '../issue-input/IssueInputPage';
import BasePageElement from '../../arch/BasePageElement';
import { IssueSearchConditionModel } from '../../api/Api';

export default class IssueListPage {
  private issueListPageEl: IssueListPageElement;

  constructor(page: BasePageElement) {
    this.issueListPageEl = new IssueListPageElement(page);
  }

  async gotoNewIssuePage() {
    await this.issueListPageEl.clickNewIssueLink();
    return new IssueInputPage(this.issueListPageEl);
  }

  async gotoIssueBySubject(subject: string) {
    await this.issueListPageEl.clickIssueNoLinkBySubject(subject);
    return new IssueInputPage(this.issueListPageEl);
  }

  async searchIssue(condition: IssueSearchConditionModel) {
    await this.issueListPageEl.inputSearch(condition.text);
    await this.issueListPageEl.clickSearchBtn();
  }
}
