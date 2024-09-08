import BasePage from '../arch/BasePage';

export default class TopPage extends BasePage {
  get pageNameKey() {
    return 'home';
  }

  async open() {
    await super.open('/');
  }
}
