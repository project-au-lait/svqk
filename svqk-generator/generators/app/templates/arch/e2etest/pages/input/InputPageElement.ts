<% include('../../../../lib/data-factory-def', { fields }); -%>
<%_
const inputDef = (field) => {
  const required = field.required ? '' : '?';
  const type = field.javaType === 'Integer' ? 'number' : 'string';
  return `async input${field.fieldNmPascal}(${field.fieldName}${required}: ${type}) {`;
}

const inputImpl = (field) => {
  const required = field.required ? '' : '?? \'\'';
  return `await this.inputText('#${field.fieldName}', ${field.fieldName}${required});`;
}

const expectDef = (field) => {
  const required = field.required ? '' : '?';
  const type = field.javaType === 'Integer' ? 'number' : 'string';
  return `async expect${field.fieldNmPascal}(${field.fieldName}${required}: ${type}) {`;
}

const expectImpl = (field) => {
  const required = field.required ? '' : '?? \'\'';
  return `await this.expectText('#${field.fieldName}', ${field.fieldName}${required});`;
}
%>
import BasePageElement from '@arch/BasePageElement';
import { t } from '@arch/MultiLng';

export default class <%= entityNmPascal %>InputPageElement extends BasePageElement {
  get pageNameKey() {
    return 'new<%= entityNmPascal %>';
  }

  <%_ for (field of compIdFields || [idField]) { _%>
  <%= inputDef(field) %>
    <%- inputImpl(field) %>
  }    
  <%_ } _%>
  
  <%_ for (field of nonIdFields) { _%>
  <%= inputDef(field) %>
    <%- inputImpl(field) %>
  }
  <%_ } _%>

  <%_ for (field of compIdFields || [idField]) { _%>
  <%= expectDef(field) %>
    <%- expectImpl(field) %>
  }
  <%_ } _%>

  <%_ for (field of nonIdFields) { _%>
  <%= expectDef(field) %>
    <%- expectImpl(field) %>
  }
  <%_ } _%>

  async clickSaveBtn() {
    await this.click('#save');
    await this.expectGlobalMessage(t('saved'));
  }
}