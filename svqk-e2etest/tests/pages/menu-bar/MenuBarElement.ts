import BasePageElement from '../../arch/BasePageElement';

export default class MenuBarElement extends BasePageElement {
  get pageNameKey(): string {
    return 'MenuBar';
  }

  async clickIssueLink() {
    await this.click('#issue');
  }
}
