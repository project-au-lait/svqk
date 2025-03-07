import BasePageElement from '@arch/BasePageElement';

export default class MenuBarPageElement extends BasePageElement {
  get pageNameKey(): string {
    return 'MenuBar';
  }

  async clickIssueLink() {
    await this.click('#issue');
  }

  /**
   * The following __PLACEHOLDER__ comments are dynamically replaced.
   * Please do not delete.
   */
  /* __PLACEHOLDER__ */
}
