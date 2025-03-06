import BasePageElement from '@arch/BasePageElement';
import MenuBarPageElement from '@pages/menu-bar/MenuBarPageElement';
import IssueListPage from '@pages/issue-list/IssueListPage';

export default class MenuBar {
  private readonly menuBarEl: MenuBarPageElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarPageElement(page);
  }

  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }
}
