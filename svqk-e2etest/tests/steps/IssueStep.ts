import type { Page } from '@playwright/test';
import IssueInputOperation from '../operations/IssueInputOperation';
import IssueListOperation from '../operations/IssueListOperation';
import IssueInputFactory from '../factories/IssueFormFactory';
import type { IssueModel } from '../api/Api';
import BaseStep from '../arch/BaseStep';
import { DryRun } from '../arch/DryRun';
import TopOperation from '../operations/TopOperation';

export class IssueStep extends BaseStep {
  private issueInput: IssueInputOperation;
  private issueList: IssueListOperation;
  private top: TopOperation;

  constructor(page: Page, dryRun: DryRun) {
    super(dryRun);
    this.top = new TopOperation(page, dryRun);
    this.issueInput = new IssueInputOperation(page, dryRun);
    this.issueList = new IssueListOperation(page, dryRun);
  }

  async createIssue() {
    this.startStep('チケットの登録');
    const issue = IssueInputFactory.createRandomForm();
    await this.top.gotoTop();
    await this.issueInput.gotoNewIssue();
    await this.issueInput.inputForm(issue);
    return issue;
  }

  async searchIssue(subject: string) {
    this.startStep('チケットの検索');
    await this.issueList.gotoIssueList();
    await this.issueList.searchIssue(subject);
  }

  async referenceIssue(issue: IssueModel) {
    this.startStep('チケットの参照');
    await this.issueList.gotoIssueList();
    await this.issueList.gotoIssue(issue.subject);
    await this.issueInput.expectIssue(issue);
  }

  async updateIssue() {
    this.startStep('チケットの更新');
    const issueToUpdate = IssueInputFactory.createRandomForm();
    await this.issueInput.inputForm(issueToUpdate);
    await this.issueList.gotoIssueList();
    await this.issueList.gotoIssue(issueToUpdate.subject);
    await this.issueInput.expectIssue(issueToUpdate);
  }
}
