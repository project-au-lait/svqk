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
  const issueFacade = new IssueFacade(dryRun);
  const topPage = new TopPage(page, dryRun);
  const issue = IssueInputFactory.createRandomIssue();

  const menuBar = await topPage.openTopPage();

  await issueFacade.createIssue(menuBar, issue);

  const issueInputPage = await issueFacade.referenceIssueBySubject(menuBar, issue.subject);

  const updatingIssue = IssueInputFactory.createRandomIssue();

  await issueFacade.updateIssue(issueInputPage, updatingIssue);

  await issueFacade.expectIssue(issueInputPage, updatingIssue);

  // TODO: Add issue delete step
});
