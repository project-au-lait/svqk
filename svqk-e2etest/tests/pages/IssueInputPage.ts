import type { IssueModel } from '../api/Api';
import { t } from '../arch/MultiLng';
import BasePage from '../arch/BasePage';

export default class IssueInputPage extends BasePage {
  get pageName() {
    return 'チケット入力画面';
  }

  async gotoInput() {
    await this.click('#issue', 'チケットリンク');
    await this.click('#newIssue', '新しいチケットリンク');
  }

  async inputSubject(subject: string) {
    await this.inputText('#subject', '題名', subject);
  }

  async inputDescription(description: string) {
    await this.inputText('#description', '説明', description);
  }

  async save() {
    await this.click('#save', '登録 or 更新ボタン');
    await this.expectGlobalMessage(t('msg.saved'));
  }

  async expectIssueDetail(issue: IssueModel) {
    await this.expectText('#subject', '題名', issue.subject);
    await this.expectText('#description', '説明', issue.description!);
  }
}
