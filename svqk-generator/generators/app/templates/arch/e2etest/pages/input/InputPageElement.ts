<%_
const inputDef = (field) => {
  const required = field.required ? '' : '?';
  const typeMap = {
    'Integer': 'number',
    'String': 'string',
    'java.time.LocalDate': 'LocalDate',
    'java.time.LocalDateTime': 'LocalDateTime',
    'Boolean': 'boolean'
  };
  let type = typeMap[field.javaType] || 'any';

  if (field.multiple === true) {
    type += '[]'
  }
  return `async input${field.fieldNmPascal}(${field.fieldName}${required}: ${type}) {`;
}

const inputImpl = (field) => {
  const required = field.required ? '' : field.javaType === 'Boolean' ? '?? false' : "?? ''";

  const inputMethodMap = {
    'Integer': 'inputText',
    'String': 'inputText',
    'java.time.LocalDate': 'inputText',
    'java.time.LocalDateTime': 'inputText',
    'Boolean': 'check'
  };
  const inputMethod = inputMethodMap[field.javaType] || 'expectSelectedOption';
  return `await this.${inputMethod}('#${field.fieldName}', ${field.fieldName}${required});`;
}

const expectDef = (field) => {
  const required = field.required ? '' : '?';
  const typeMap = {
    'Integer': 'number',
    'String': 'string',
    'java.time.LocalDate': 'LocalDate',
    'java.time.LocalDateTime': 'LocalDateTime',
    'Boolean': 'boolean'
  };
  let type = typeMap[field.javaType] || 'any';

  if (field.multiple === true) {
    type += '[]'
  }
  return `async expect${field.fieldNmPascal}(${field.fieldName}${required}: ${type}) {`;
}

const expectImpl = (field) => {
  const required = field.required ? '' : field.javaType === 'Boolean' ? '?? false' : "?? ''";

  const inputMethodMap = {
    'Integer': 'expectText',
    'String': 'expectText',
    'java.time.LocalDate': 'expectText',
    'java.time.LocalDateTime': 'expectText',
    'Boolean': 'expectChecked'
  }
  const inputMethod = inputMethodMap[field.javaType] || 'expectSelectedOption';
  return `await this.${inputMethod}('#${field.fieldName}', ${field.fieldName}${required});`;
}
%>
import { LocalDate, LocalDateTime } from '@api/Api';
import BasePageElement from '@arch/BasePageElement';
import * as m from '@paraglide/messages';

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
  }

  async expectSavedSuccessfully() {
    await this.expectGlobalMessage(m.saved());
  }

  async clickDeleteBtn() {
    await this.click('#del');
  }

  async expectDeletedSuccessfully() {
    await this.expectGlobalMessage(m.deleted());
  }
}
