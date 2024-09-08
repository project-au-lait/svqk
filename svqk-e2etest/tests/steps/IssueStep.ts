import IssueInputOperation from '../operations/IssueInputOperation';
import type { IssueModel } from '../api/Api';
import BaseStep from '../arch/BaseStep';
import MenuOperation from '../operations/MenuOperation';

export class IssueStep extends BaseStep {
  async createIssue(menuOp: MenuOperation, issue: IssueModel) {
    this.startStep('チケットの登録');
    const issueListOp = await menuOp.gotoIssueListPage();
    const issueInputOp = await issueListOp.gotoNewIssuePage();
    await issueInputOp.save(issue);
    return issueInputOp;
  }

  async referenceIssueBySubject(menuOp: MenuOperation, subject: string) {
    this.startStep('チケットの照会');
    const issueListOp = await menuOp.gotoIssueListPage();
    await issueListOp.searchIssueBySubject(subject);
    const issueInputOp = await issueListOp.gotoIssueBySubject(subject);

    return issueInputOp;
  }

  async updateIssue(issueInputOp: IssueInputOperation, issue: IssueModel) {
    this.startStep('チケットの更新');
    await issueInputOp.save(issue);
  }

  async expectIssue(issueInputOp: IssueInputOperation, issue: IssueModel) {
    await issueInputOp.expectIssue(issue);
  }
}
