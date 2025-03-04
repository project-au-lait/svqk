<% include('../../../../lib/data-factory-def', { fields }); -%>
import <%= entityNmPascal %>InputPageElement from './<%= entityNmPascal %>InputPageElement';
import type { <%= entityNmPascal %>Model } from '../../api/Api';
import BasePageElement from '../../arch/BasePageElement';

export default class <%= entityNmPascal %>InputPage {
  private <%= entityNmCamel %>InputPageEl: <%= entityNmPascal %>InputPageElement;

  constructor(page: BasePageElement) {
    this.<%= entityNmCamel %>InputPageEl = new <%= entityNmPascal %>InputPageElement(page);
  }

  async save(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ fields.forEach((field) => { _%>
      <%_ if (field.javaType === 'Integer' || field.javaType === 'String') { _%>
    await this.<%= entityNmCamel %>InputPageEl.input<%= getFieldNmPascal(field.fieldName) %>(<%= entityNmCamel %>.<%= field.fieldName %><%_ if (field.javaType === 'Integer') { _%>.toString()<%_ } _%><%_ if (!field.required) { _%> ?? ''<%_ } _%>);
      <%_ } _%>
    <%_ }) _%>

    await this.<%= entityNmCamel %>InputPageEl.clickSaveBtn();
  }

  async expect<%= entityNmPascal %>(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ fields.forEach((field) => { _%>
      <%_ if (field.javaType === 'Integer' || field.javaType === 'String') { _%>
    await this.<%= entityNmCamel %>InputPageEl.expect<%= getFieldNmPascal(field.fieldName) %>(<%= entityNmCamel %>.<%= field.fieldName %><%_ if (field.javaType === 'Integer') { _%>.toString()<%_ } _%>);
      <%_ } _%>
    <%_ }) _%>
  }
}