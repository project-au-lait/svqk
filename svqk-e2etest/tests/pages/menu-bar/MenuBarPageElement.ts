/**
 * The section marked with "__****__" is dynamically replaced.
 * Do not remove or modify it.
 */
import BasePageElement from '@arch/BasePageElement';

export default class MenuBarPageElement extends BasePageElement {
  get pageNameKey(): string {
    return 'MenuBar';
  }

  async clickIssueLink() {
    await this.click('#issue');
  }

  /* __CLICK__ */
}
