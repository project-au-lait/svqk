import MenuPage from './MenuPage';

export default class TopPage extends MenuPage {
  get pageName() {
    return 'トップ画面';
  }

  async open() {
    await super.open('/');
  }
}
