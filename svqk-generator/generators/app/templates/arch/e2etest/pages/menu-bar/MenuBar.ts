import BasePageElement from '@arch/BasePageElement';
import MenuBarPageElement from '@pages/menu-bar/MenuBarPageElement';
import IssueListPage from '@pages/issue-list/IssueListPage';
<%_ if (entityNmPascal !== "Issue") { _%>
import <%= entityNmPascal %>ListPage from '@pages/<%= entityNmKebab %>-list/<%= entityNmPascal %>ListPage';
<%_ } _%>

export default class MenuBar {
  private menuBarEl: MenuBarPageElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarPageElement(page);
  }

  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }

  <%_ if (entityNmPascal !== "Issue") { _%>
  async goto<%= entityNmPascal %>ListPage() {
    await this.menuBarEl.open<%= entityNmPascal %>ListPage();
    return new <%= entityNmPascal %>ListPage(this.menuBarEl);
  }
  <%_ } _%>
}