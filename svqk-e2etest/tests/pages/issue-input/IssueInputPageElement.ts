import BasePageElement from '../../arch/BasePageElement';
import { t } from '../../arch/MultiLng';

export default class IssueInputPageElement extends BasePageElement {
  get pageNameKey() {
    return 'newIssue';
  }

  async inputSubject(subject: string) {
    await this.inputText('#subject', subject);
  }

  async inputDescription(description: string) {
    await this.inputText('#description', description);
  }

  async clickSaveBtn() {
    await this.click('#save');
  }

  async expectSavedSuccessfully() {
    await this.expectGlobalMessage(t('saved'));
  }

  async expectSubject(subject: string) {
    await this.expectText('#subject', subject);
  }

  async expectDescription(description?: string) {
    await this.expectText('#description', description ?? '');
  }

  async clickDeleteBtn() {
    await this.click('#deleteIssue');
  }

  async expectDeletedSuccessfully() {
    await this.expectGlobalMessage(t('deleted'));
  }
}
