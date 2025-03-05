import BasePageElement from '@arch/BasePageElement';

export default class IssueListPageElement extends BasePageElement {
  get pageNameKey() {
    return 'issue';
  }

  async clickNewIssueLink() {
    await this.click('#newIssue');
  }

  async clickIssueNoLinkBySubject(subject: string) {
    await this.clickInRow(subject);
  }

  async inputSearch(searchValue: string) {
    await this.inputText('input[type="search"]', searchValue);
  }

  async clickSearchBtn() {
    await this.click('input[type="submit"]');
  }

  async expectSearchResult(expectValue: string) {
    await this.expectTdTextExists(expectValue);
  }
}
