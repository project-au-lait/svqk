import BasePage from '../arch/BasePage';
import MenuBar from '../pages/MenuBar';
import IssueListOperation from './IssueListOperation';

export default class MenuOperation {
  private menuPage: MenuBar;

  constructor(page: BasePage) {
    this.menuPage = new MenuBar(page);
  }

  async gotoIssueListPage() {
    await this.menuPage.clickIssueLink();
    return new IssueListOperation(this.menuPage);
  }
}
