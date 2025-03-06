import BasePageElement from '../../arch/BasePageElement';
import MenuBarPageElement from './MenuBarPageElement';
import IssueListPage from '../issue-list/IssueListPage';
/* importEntityListPage */

export default class MenuBar {
  private menuBarEl: MenuBarPageElement;

  constructor(page: BasePageElement) {
    this.menuBarEl = new MenuBarPageElement(page);
  }

  async gotoIssueListPage() {
    await this.menuBarEl.clickIssueLink();
    return new IssueListPage(this.menuBarEl);
  }

  /* gotoEntityListPage */
}
