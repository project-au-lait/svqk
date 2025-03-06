import type { <%= entityNmPascal %>Model } from '@api/Api';
import StringUtils from '@arch/StringUtils';

export default class <%= entityNmPascal %>Factory {
  static create() {
    const <%= entityNmCamel %> = {} as <%= entityNmPascal %>Model;
    return <%= entityNmCamel %>;
  }

  static createRandom<%= entityNmPascal %>(id: number) {
    const <%= entityNmCamel %> = this.create();
    <%_ for (field of fields) { _%>
      <%_ const fieldType = field.javaType; _%>
      <%_ if (field.fieldName === "id") { _%>
    <%= entityNmCamel %>.<%= field.fieldName %> = id;
      <%_ continue;} _%>
      <%_ if (fieldType === "String") { _%>
    <%= entityNmCamel %>.<%= field.fieldName %> = StringUtils.generateRandomString();
      <%_ } else if (fieldType === "Integer") { _%>
    <%= entityNmCamel %>.<%= field.fieldName %> = NumberUtils.generateRandomNumber();
      <%_ } _%>
    <%_ } _%>
    return <%= entityNmCamel %>;
  }
}