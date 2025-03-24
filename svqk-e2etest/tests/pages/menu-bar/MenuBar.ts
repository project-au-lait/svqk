/**
 * The section marked with "__PLACEHOLDER__" is dynamically replaced.
 * Do not remove or modify it.
 */
import BasePageElement from '@arch/BasePageElement';
import MenuBarPageElement from '@pages/menu-bar/MenuBarPageElement';
// ==FOR_REFIMPL==>
import IssueListPage from '@pages/issue-list/IssueListPage';
// <==FOR_REFIMPL==
/* __PLACEHOLDER__:import */

export default class MenuBar {
  private readonly menuBarEl: MenuBarPageElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarPageElement(page);
  }

  // ==FOR_REFIMPL==>
  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }
  // <==FOR_REFIMPL==

  /* __PLACEHOLDER__ */
}
