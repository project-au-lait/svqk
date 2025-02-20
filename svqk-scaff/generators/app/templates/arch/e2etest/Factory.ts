import type { <%= entityNmPascal %>Model } from '../api/Api';
import StringUtils from '../arch/StringUtils';
import NumberUtils from '../arch/NumberUtils';

export default class <%= entityNmPascal %>Factory {
  static create() {
    const <%= entityNmCamel %> = {} as <%= entityNmPascal %>Model;
    return <%= entityNmCamel %>;
  }

  static createRandom<%= entityNmPascal %>() {
    const <%= entityNmCamel %> = this.create();
    <%_ for (field of fields) { _%>
      <%_ const fieldType = field.javaType; _%>
      <%_ if (fieldType === "String") { _%>
        <%= entityNmCamel %>.<%= field.fieldName %> = StringUtils.generateRandomString();
      <%_ } else if (fieldType === "Integer") { _%>
        <%= entityNmCamel %>.<%= field.fieldName %> = NumberUtils.generateRandomNumber();
      <%_ } _%>
    <%_ } _%>
    return <%= entityNmCamel %>;
  }
}
