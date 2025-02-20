import <%= entityNmPascal %>InputPageElement from './<%= entityNmPascal %>InputPageElement';
import type { <%= entityNmPascal %>Model } from '../../api/Api';
import BasePageElement from '../../arch/BasePageElement';

export default class <%= entityNmPascal %>InputPage {
  private <%= entityNmCamel %>InputPageEl: <%= entityNmPascal %>InputPageElement;

  constructor(page: BasePageElement) {
    this.<%= entityNmCamel %>InputPageEl = new <%= entityNmPascal %>InputPageElement(page);
  }

  async save(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ for (field of fields) { _%>
      await this.<%= entityNmCamel %>InputPageEl.input<%= getFieldNmPascal(field.fieldName) %>(<%= entityNmCamel %>.<%= field.fieldName %>);
    <%_ } _%>

    await this.<%= entityNmCamel %>InputPageEl.clickSaveBtn();
  }

  async expect<%= entityNmPascal %>(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ for (field of fields) { _%>
      await this.<%= entityNmCamel %>InputPageEl.expect<%= getFieldNmPascal(field.fieldName) %>(<%= entityNmCamel %>.<%= field.fieldName %>);
    <%_ } _%>
  }
}