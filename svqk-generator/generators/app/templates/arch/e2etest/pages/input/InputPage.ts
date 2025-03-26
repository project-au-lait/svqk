<%_
const inputImpl = (field, isId) => {
  const id = isId ? '.id' : '';
  return `await this.${entityNmCamel}InputPageEl.input${field.fieldNmPascal}(${entityNmCamel}${id}.${field.fieldName});`;
}
%>
import <%= entityNmPascal %>InputPageElement from '@pages/<%= entityNmKebab %>-input/<%= entityNmPascal %>InputPageElement';
import type { <%= entityNmPascal %>Model } from '@api/Api';
import BasePageElement from '@arch/BasePageElement';

export default class <%= entityNmPascal %>InputPage {
  private <%= entityNmCamel %>InputPageEl: <%= entityNmPascal %>InputPageElement;

  constructor(page: BasePageElement) {
    this.<%= entityNmCamel %>InputPageEl = new <%= entityNmPascal %>InputPageElement(page);
  }

  async create(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ for (field of compIdFields || [idField]) { _%>
    <%- inputImpl(field, compIdFields) %>
    <%_ } _%>  
    <%_ for (field of nonIdFields) { _%>
    <%- inputImpl(field, false) %>
    <%_ } _%>
    
    await this.<%= entityNmCamel %>InputPageEl.clickSaveBtn();
  }

  async update(<%= entityNmCamel %>: <%= entityNmPascal %>Model) { 
    <%_ for (field of nonIdFields) { _%>
    <%- inputImpl(field, false) %>
    <%_ } _%>
    
    await this.<%= entityNmCamel %>InputPageEl.clickSaveBtn();
  }

  async expectSavedSuccessfully() {
    await this.<%= entityNmCamel %>InputPageEl.expectSavedSuccessfully();
  }

  async expect<%= entityNmPascal %>(<%= entityNmCamel %>: <%= entityNmPascal %>Model) {
    <%_ for (field of nonIdFields) { _%>
      <%_ if (field.javaType === 'Integer' || field.javaType === 'String') { _%>
    await this.<%= entityNmCamel %>InputPageEl.expect<%= field.fieldNmPascal %>(<%= entityNmCamel %>.<%= field.fieldName %>);
      <%_ } _%>
    <%_ } _%>
  }

  async delete() {    
    await this.<%= entityNmCamel %>InputPageEl.clickDeleteBtn();
  }

  async expectDeletedSuccessfully() {
    await this.<%= entityNmCamel %>InputPageEl.expectDeletedSuccessfully();
  }
}