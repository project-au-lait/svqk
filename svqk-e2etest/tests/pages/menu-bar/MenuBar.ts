import BasePageElement from '@arch/BasePageElement';
import MenuBarPageElement from '@pages/menu-bar/MenuBarPageElement';
import IssueListPage from '@pages/issue-list/IssueListPage';
/**
 * The following __PLACEHOLDER__ comments are dynamically replaced.
 * Please do not delete.
 */
/* __PLACEHOLDER__:import */

export default class MenuBar {
  private menuBarEl: MenuBarPageElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarPageElement(page);
  }

  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }

  /**
   * The following __PLACEHOLDER__ comments are dynamically replaced.
   * Please do not delete.
   */
  /* __PLACEHOLDER__ */
}
