<% include('../../../../../lib/data-factory-def', { fields }); -%>
import BasePageElement from '../../arch/BasePageElement';
import { t } from '../../arch/MultiLng';

export default class <%= entityNmPascal %>InputPageElement extends BasePageElement {
  get pageNameKey() {
    return 'new<%= entityNmPascal %>';
  }

  <%_ for (field of fields) { _%>
    async input<%= getFieldNmPascal(field.fieldName) %>(<%= field.fieldName %>: string) {
      await this.inputText('#<%= field.fieldName %>', <%= field.fieldName %>);
    }
  <%_ } _%>

  <%_ for (field of fields) { _%>
    <%_ if (field.required) { _%>
      async expect<%= getFieldNmPascal(field.fieldName) %>(<%= field.fieldName %>: string) {
        await this.expectText('#<%= field.fieldName %>', <%= field.fieldName %>);
      }
    <%_ } else { _%>
      async expect<%= getFieldNmPascal(field.fieldName) %>(<%= field.fieldName %>?: string) {
        await this.expectText('#<%= field.fieldName %>', <%= field.fieldName %> ?? '');
      }
    <%_ } _%>
  <%_ } _%>

  async clickSaveBtn() {
    await this.click('#save');
    await this.expectGlobalMessage(t('saved'));
  }
}
