<% include('../../lib/data-factory-def', { fields }); -%>
<% include('../../lib/frontend-common', { entityNmPascal, compIdFields }); -%>
<%_
w = (num) => " ".repeat(num);

fieldToProperty = (fields, num = 6) => {
  return fields.map((field) => {
    const fieldType = field.javaType;
    if (fieldType === "String") {
      return `${w(num)}${field.fieldName}: StringUtils.generateRandomString()`;
    } else if (fieldType === "Integer") {
      return `${w(num)}${field.fieldName}: NumberUtils.generateRandomNumber()`;
    }
  }).filter((property) => property).join(",\n");
};

idProperty = compIdFields ? `id: {\n${fieldToProperty(compIdFields, 8)}\n${w(6)}},` : "";
_%>
import type { <%= entityNmPascal %>Model } from '@api/Api';
import StringUtils from '@arch/StringUtils';
import NumberUtils from '@arch/NumberUtils';
<%- importDecIdTypeE2etest %>

export default class <%= entityNmPascal %>Factory {
  static create() {
    const <%= entityNmCamel %> = {} as <%= entityNmPascal %>Model;
    return <%= entityNmCamel %>;
  }

  static createRandom<%= entityNmPascal %>() {
    return {
      <%= idProperty %>
<%= fieldToProperty(fields) %>
    } as <%= entityNmPascal %>Model;
  }

  static createRandom<%= entityNmPascal %>WithId(id : <%= idType %>) {
    return {
      id,
<%= fieldToProperty(nonIdFields) %>
    } as <%= entityNmPascal %>Model;
  }

}