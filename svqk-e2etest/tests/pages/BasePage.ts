import { expect, type Page } from '@playwright/test';
import { Action, DryRun } from '../arch/DryRun';

export default abstract class BasePage {
  constructor(protected page: Page, private dryRun: DryRun) {}

  abstract get pageName(): string;

  private async run(
    action: Action,
    itemName: string,
    runAction: () => Promise<any>,
    additionalValue?: string
  ) {
    const log = this.dryRun.log(this.pageName, itemName, action, additionalValue);

    if (this.dryRun.isOn) {
      return;
    }

    await this.consoleLog(log);

    await runAction();
  }

  private async consoleLog(log: string) {
    await this.page.evaluate("console.log('" + log + "');");
  }

  protected async inputText(selector: string, itemName: string, value: string) {
    await this.run(Action.INPUT, itemName, () => this.page.locator(selector).fill(value), value);
  }

  protected async goTo(path: string) {
    await this.run(Action.GOTO, path, () => this.page.goto(path));
  }

  protected async click(selector: string, itemName: string) {
    await this.run(Action.CLICK, itemName, () => this.page.locator(selector).click());
  }

  protected async getTd(selector: string) {
    return await this.page.locator(`td${selector}`);
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

  protected async expectText(selector: string, itemName: string, value: string) {
    await this.run(
      Action.EXPECT_TEXT,
      itemName,
      () => expect(this.page.locator(selector)).toHaveValue(value),
      value
    );
  }

  protected async clickInRow(text: string, itemName: string) {
    await this.expectTdTextExists(text);
    await this.click(`tr:has(td:has-text("${text}")) a`, itemName);
  }
}
