import BasePage from '../arch/BasePage';
import { t } from '../arch/MultiLng';

export default class IssueInputPage extends BasePage {
  get pageName() {
    return 'チケット入力画面';
  }

  async inputSubject(subject: string) {
    await this.inputText('#subject', '題名', subject);
  }

  async inputDescription(description: string) {
    await this.inputText('#description', '説明', description);
  }

  async clickSaveBtn() {
    await this.click('#save', '登録 or 更新ボタン');
    await this.expectGlobalMessage(t('msg.saved'));
  }

  async expectSubject(subject: string) {
    await this.expectText('#subject', '題名', subject);
  }

  async expectDescription(description?: string) {
    await this.expectText('#description', '説明', description ?? '');
  }
}
