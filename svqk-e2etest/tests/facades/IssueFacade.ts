import BaseFacade from '@arch/BaseFacade';
import MenuBar from '@pages/menu-bar/MenuBar';

export class IssueFacade extends BaseFacade {
  // <.>
  async referenceIssueBySubject(menuBar: MenuBar, subject: string) {
    this.logStart('Issue Reference');

    // <.>
    const issueListPage = await menuBar.gotoIssueListPage();
    await issueListPage.searchIssue({ text: subject });
    const issueInputPage = await issueListPage.gotoIssueBySubject(subject);

    // <.>
    return issueInputPage;
  }
}
