<%_ include('../../../../lib/frontend-common', { entityNmPascal, compIdFields }); -%>
import BasePageElement from '@arch/BasePageElement';
<%- importDecIdTypeE2etest %>

export default class <%= entityNmPascal %>ListPageElement extends BasePageElement {
  get pageNameKey() {
    return '<%= entityNmCamel %>';
  }

  async clickNew<%= entityNmPascal %>Link() {
    await this.click('#new<%= entityNmPascal %>');
  }

  async click<%= entityNmPascal %>NoLinkById(id: <%= idType %>) {
    <%_
    const id = compIdFields ? compIdFields.map((field) => `id.${field.fieldName}`).join(" + '/' + ") : "id";
    -%>
    await this.clickInRowID(<%- id %>);
  }
}