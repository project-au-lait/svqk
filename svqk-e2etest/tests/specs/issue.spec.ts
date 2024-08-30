import { test } from '@playwright/test';
import { IssueStep } from '../steps/IssueStep';
import { DryRun } from '../arch/DryRun';
import { locale } from '../arch/MultiLng';

test.describe('Issue', () => {
  test('CRUD of Issue', async ({ browser }) => {
    const context = await browser.newContext({
      locale: locale
    });
    const page = await context.newPage();

    const issueStep = new IssueStep(page, DryRun.build());

    const issue = await issueStep.createIssue();

    await issueStep.searchIssue(issue.subject);

    await issueStep.referenceIssue(issue);

    await issueStep.updateIssue();

    // TODO: Add issue delete step
  });
});
