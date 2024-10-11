import BasePageElement from '../../arch/BasePageElement';

export default class MenuBarPageElement extends BasePageElement {
  get pageNameKey(): string {
    return 'MenuBar';
  }

  async clickIssueLink() {
    await this.click('#issue');
  }
}
