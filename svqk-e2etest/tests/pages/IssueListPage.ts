import MenuPage from './MenuPage';

export default class IssueListPage extends MenuPage {
  get pageName() {
    return 'チケット一覧画面';
  }

  async clickNewIssueLink() {
    await this.click('#newIssue', '新しいチケットリンク');
  }

  async clickIssueNoLinkBySubject(subject: string) {
    await this.clickInRow(subject, 'チケット番号リンク');
  }

  async inputSearch(searchValue: string) {
    await this.inputText('input[type="search"]', '検索ワード', searchValue);
  }

  async clickSearchBtn() {
    await this.click('input[type="submit"]', '検索ボタン');
  }

  async expectSearchResult(expectValue: string) {
    await this.expectTdTextExists(expectValue);
  }
}
