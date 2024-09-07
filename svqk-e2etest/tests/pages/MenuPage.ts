import BasePage from '../arch/BasePage';

export default abstract class MenuPage extends BasePage {
  async clickIssueLink() {
    await this.click('#issue', 'チケットリンク');
  }
}
