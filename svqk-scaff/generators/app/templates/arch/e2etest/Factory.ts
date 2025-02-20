import type { <%= entityNmPascal %>Model } from '../api/Api';
import StringUtils from '../arch/StringUtils';
import NumberUtils from '../arch/NumberUtils';

export default class <%= entityNmPascal %>Factory {
  static create() {
    const <%= entityNmCamel %>: <%= entityNmPascal %>Model = {}
    return <%= entityNmCamel %>;
  }

  static createRandom<%= entityNmPascal %>() {
    const <%= entityNmCamel %> = this.create();
    <%_ for (field of fields) { _%>
      <%_ const fieldType = typeof field; _%>
      <%_ if (fieldType === "string") { _%>
        <%= entityNmCamel %>.<%= field.fieldName %> = StringUtils.generateRandomString();
      <%_ } else if (fieldType === "number") { _%>
        <%= entityNmCamel %>.<%= field.fieldName %> = NumberUtils.generateRandomNumber();
      <%_ } _%>
    <%_ } _%>
    return <%= entityNmCamel %>;
  }
}
