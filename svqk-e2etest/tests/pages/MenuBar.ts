import BasePage from '../arch/BasePage';

export default class MenuBar extends BasePage {
  get pageNameKey(): string {
    return 'MenuBar';
  }

  async clickIssueLink() {
    await this.click('#issue');
  }
}
