import { test } from '@playwright/test';
import { IssueFacade } from '../facades/IssueFacade';
import { DryRun } from '../arch/DryRun';
import { locale } from '../arch/MultiLng';
import TopPage from '../pages/top/TopPage';
import IssueInputFactory from '../factories/IssueFactory';

test('CRUD of Issue', async ({ browser }) => {
  const context = await browser.newContext({
    locale: locale
  });
  const page = await context.newPage();
  const dryRun = DryRun.build();

  const topPage = new TopPage(page, dryRun);
  const menuBar = await topPage.open();

  let issueListPage = await menuBar.gotoIssueListPage();
  let issueInputPage = await issueListPage.gotoNewIssuePage();

  // Create
  const issue = IssueInputFactory.createRandomIssue();
  await issueInputPage.save(issue);

  // Rererence
  const issueFacade = new IssueFacade(dryRun);
  issueInputPage = await issueFacade.referenceIssueBySubject(menuBar, issue.subject);

  // Update
  const updatingIssue = IssueInputFactory.createRandomIssue();
  await issueInputPage.save(updatingIssue);
  await issueInputPage.expectIssue(updatingIssue);

  // Delete
  await issueInputPage.delete();
});
