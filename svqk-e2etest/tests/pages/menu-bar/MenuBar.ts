/**
 * The section marked with "__****__" is dynamically replaced.
 * Do not remove or modify it.
 */
import BasePageElement from '@arch/BasePageElement';
import MenuBarPageElement from '@pages/menu-bar/MenuBarPageElement';
// ==FOR_REFIMPL==>
import IssueListPage from '@pages/issue-list/IssueListPage';
// <==FOR_REFIMPL==
/* __IMPORT__ */

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

  /* __GOTO__ */
}
