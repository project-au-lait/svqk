import BasePage from '../arch/BasePage';

export default class TopPage extends BasePage {
  get pageName() {
    return 'トップ画面';
  }

  async open() {
    await super.open('/');
  }
}
