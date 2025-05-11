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

  protected async inputText(selector: string, value: any) {
    await this.run(
      Action.INPUT,
      selector,
      () => this.page.locator(selector).fill(value.toString()),
      value
    );
  }

  protected async selectOption(selector: string, value: any) {
    await this.run(
      Action.INPUT,
      selector,
      () => this.page.locator(selector).selectOption(value),
      value
    );
  }

  protected async check(selector: string, value: boolean) {
    await this.run(Action.INPUT, selector, () => this.page.locator(selector).setChecked(value));
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
    await this.run(Action.EXPECT_VISIBLE, text, () =>
      expect(this.page.locator(`td:has-text("${text}")`)).toBeVisible()
    );
  }

  protected async expectText(selector: string, value: any) {
    await this.page.waitForSelector(selector);
    await this.run(
      Action.EXPECT_TEXT,
      selector,
      () => expect(this.page.locator(selector)).toHaveValue(value.toString()),
      value
    );
  }

  protected async expectSelectedOption(selector: string, value: any) {
    await this.page.waitForSelector(selector);
    const runAction = Array.isArray(value)
      ? () => expect(this.page.locator(selector)).toHaveValues(value.map((v) => v.toString()))
      : () => expect(this.page.locator(selector)).toHaveValue(value.toString());
    await this.run(Action.EXPECT_TEXT, selector, runAction, value);
  }

  protected async expectChecked(selector: string, value: boolean) {
    await this.page.waitForSelector(selector);
    const runAction = value
      ? () => expect(this.page.locator(selector)).toBeChecked()
      : () => expect(this.page.locator(selector)).not.toBeChecked();
    await this.run(Action.EXPECT_TEXT, selector, runAction);
  }

  protected async clickInRow(text: any) {
    await this.click(`tr:has(td:has-text("${text}")) a`);
  }
}
