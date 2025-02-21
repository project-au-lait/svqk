import BasePageElement from '../../arch/BasePageElement';
import MenuBarPageElement from './MenuBarPageElement';
import IssueListPage from '../issue-list/IssueListPage';
import <%= entityNmPascal %>ListPage from '../<%= entityNmCamel %>-list/<%= entityNmPascal %>ListPage';

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
      await this.menuBarEl.click<%= entityNmPascal %>Link();
      return new <%= entityNmPascal %>ListPage(this.menuBarEl);
    }
  <%_ } _%>
}