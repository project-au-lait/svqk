import { Page } from '@playwright/test';
import TopPage from '../pages/TopPage';
import { DryRun } from '../arch/DryRun';
import MenuOperation from './MenuOperation';

export default class TopOperation {
  private topPage: TopPage;

  constructor(page: Page, dryRun: DryRun) {
    this.topPage = new TopPage({ page, dryRun });
  }

  async openTopPage() {
    await this.topPage.open();
    return new MenuOperation(this.topPage);
  }
}
