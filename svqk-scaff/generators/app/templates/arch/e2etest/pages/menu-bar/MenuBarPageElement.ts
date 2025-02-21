import BasePageElement from "../../arch/BasePageElement";

export default class MenuBarPageElement extends BasePageElement {
  get pageNameKey(): string {
    return "MenuBar";
  }

  async clickIssueLink() {
    await this.click("#issue");
  }

  <%_ if (entityNmPascal !== "Issue") { _%>
    async click<%= entityNmPascal %>Link() {
      await this.click("#<%= entityNmCamel %>");
    }
  <%_ } _%>
}
