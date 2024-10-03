import BasePageElement from '../../arch/BasePageElement';
import MenuBarElement from './MenuBarElement';
import IssueListPage from '../issue-list/IssueListPage';

export default class MenuBar {
  private menuBarEl: MenuBarElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarElement(page);
  }

  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }
}
