import BasePageElement from '@arch/BasePageElement';

export default class TopPageElement extends BasePageElement {
  get pageNameKey() {
    return 'home';
  }

  async open() {
    await super.open('/');
  }
}
