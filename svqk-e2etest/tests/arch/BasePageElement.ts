import { expect, type Page } from '@playwright/test';
import { Action, DryRun } from '@arch/DryRun';

export default abstract class BasePageElement {
  page: Page;
  dryRun: DryRun;

  constructor(page: BasePageElement | { page: Page; dryRun: DryRun }) {
    this.page = page.page;
    this.dryRun = page.dryRun;
  }

  abstract get pageNameKey(): string;

  private async run(
    action: Action,
    itemNameKey: string,
    runAction: () => Promise<any>,
    additionalValue?: string
  ) {
    const log = this.dryRun.log(this.pageNameKey, itemNameKey, action, additionalValue);

    if (this.dryRun.isOn) {
      return;
    }

    await this.consoleLog(log);

    await runAction();
  }

  private async consoleLog(log: string) {
    await this.page.evaluate(`console.log(\`${log}\`);`);
  }

  protected async inputText(selector: string, value: string) {
    await this.run(Action.INPUT, selector, () => this.page.locator(selector).fill(value), value);
  }

  protected async open(path: string) {
    await this.run(Action.GOTO, path, () => this.page.goto(path));
  }

  protected async click(selector: string) {
    await this.run(Action.CLICK, selector, () => this.page.locator(selector).click());
  }

  protected async expectGlobalMessage(message: string) {
    await this.run(Action.EXPECT_VISIBLE, message, () =>
      expect(this.page.locator('#globalMessage')).toHaveText(message)
    );
  }

  protected async expectTdTextExists(text: string) {
    await this.run(Action.EXPECT_VISIBLE, text, async () => {
      let hasNextPage = true;
      while (hasNextPage) {
        hasNextPage = await this.hasNextPage();
        const isFound = await this.page.locator(`td:has-text("${text}")`).first().isVisible();
        if (isFound) {
          return;
        }
        if (hasNextPage) {
          await this.gotoNextPage();
        } else {
          throw new Error(`"${text}" not found in any pages.`);
        }
      }
    });
  }

  protected async expectText(selector: string, value: string) {
    await this.page.waitForSelector(selector);
    await this.run(
      Action.EXPECT_TEXT,
      selector,
      () => expect(this.page.locator(selector)).toHaveValue(value),
      value
    );
  }

  protected async clickInRow(text: string) {
    await this.expectTdTextExists(text);
    await this.click(`tr:has(td:has-text("${text}")) a`);
  }

  private async hasNextPage(): Promise<boolean> {
    const nextPageButton = this.page.locator('#nextPageButton')
    return await nextPageButton.isVisible() && !(await nextPageButton.isDisabled());
  }

  private async gotoNextPage() {
    await this.click('#nextPageButton');
    await this.page.waitForLoadState('domcontentloaded');
  }
}
