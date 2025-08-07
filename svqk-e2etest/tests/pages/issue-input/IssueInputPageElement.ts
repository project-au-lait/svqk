import BasePageElement from '@arch/BasePageElement';
import * as m from '../../paraglide/messages';

// <.>
export default class IssueInputPageElement extends BasePageElement {
  get pageNameKey() {
    return 'newIssue';
  }

  // <.>
  async inputSubject(subject: string) {
    await this.inputText('#subject', subject); // <.>
  }

  async inputDescription(description: string) {
    await this.inputText('#description', description);
  }

  async clickSaveBtn() {
    await this.click('#save');
  }

  async expectSavedSuccessfully() {
    await this.expectGlobalMessage(m.saved());
  }

  // <.>
  async expectSubject(subject: string) {
    await this.expectText('#subject', subject);
  }

  async expectDescription(description?: string) {
    await this.expectText('#description', description ?? '');
  }

  async clickDeleteBtn() {
    await this.click('#del');
  }

  async expectDeletedSuccessfully() {
    await this.expectGlobalMessage(m.deleted());
  }
}
