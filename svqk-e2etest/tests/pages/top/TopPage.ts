import { Page } from '@playwright/test';
import TopPageElement from '@pages/top/TopPageElement';
import { DryRun } from '@arch/DryRun';
import MenuBar from '@pages/menu-bar/MenuBar';

export default class TopPage {
  private readonly topPageEl: TopPageElement;

  constructor(page: Page, dryRun: DryRun) {
    this.topPageEl = new TopPageElement({ page, dryRun });
  }

  async open() {
    await this.topPageEl.open();
    return new MenuBar(this.topPageEl);
  }
}
