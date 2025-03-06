import BasePageElement from '@arch/BasePageElement';

export default class <%= entityNmPascal %>ListPageElement extends BasePageElement {
  get pageNameKey() {
    return '<%= entityNmCamel %>';
  }

  async clickNew<%= entityNmPascal %>Link() {
    await this.click('#new<%= entityNmPascal %>');
  }

  async click<%= entityNmPascal %>NoLinkById(id: string) {
    await this.clickInRow(id);
  }
}