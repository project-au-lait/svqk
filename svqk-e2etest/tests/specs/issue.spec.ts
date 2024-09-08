import { test } from '@playwright/test';
import { IssueStep } from '../steps/IssueStep';
import { DryRun } from '../arch/DryRun';
import { locale } from '../arch/MultiLng';
import TopOperation from '../operations/TopOperation';
import IssueInputFactory from '../factories/IssueFactory';

test.describe('Issue', () => {
  test('CRUD of Issue', async ({ browser }) => {
    const context = await browser.newContext({
      locale: locale
    });
    const page = await context.newPage();
    const dryRun = DryRun.build();
    const issueStep = new IssueStep(dryRun);
    const topOp = new TopOperation(page, dryRun);
    const issue = IssueInputFactory.createRandomIssue();

    const menuOp = await topOp.openTopPage();

    await issueStep.createIssue(menuOp, issue);

    const issueInputOp = await issueStep.referenceIssueBySubject(menuOp, issue.subject);

    const updatingIssue = IssueInputFactory.createRandomIssue();

    await issueStep.updateIssue(issueInputOp, updatingIssue);

    await issueStep.expectIssue(issueInputOp, updatingIssue);

    // TODO: Add issue delete step
  });
});
