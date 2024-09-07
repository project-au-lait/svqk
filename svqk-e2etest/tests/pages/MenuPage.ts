import BasePage from '../arch/BasePage';

export default abstract class MenuPage extends BasePage {
  async gotoIssuePage() {
    await this.click('#issue', 'チケットリンク');
  }
}
