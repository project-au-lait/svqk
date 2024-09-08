import BasePage from '../arch/BasePage';

export default class MenuBar extends BasePage {
  get pageName(): string {
    return 'menu';
  }

  async clickIssueLink() {
    await this.click('#issue', 'チケットリンク');
  }
}
