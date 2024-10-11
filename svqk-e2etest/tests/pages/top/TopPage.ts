import { Page } from '@playwright/test';
import TopPageElement from './TopPageElement';
import { DryRun } from '../../arch/DryRun';
import MenuBar from '../menu-bar/MenuBar';

export default class TopPage {
  private topPageEl: TopPageElement;

  constructor(page: Page, dryRun: DryRun) {
    this.topPageEl = new TopPageElement({ page, dryRun });
  }

  async open() {
    await this.topPageEl.open();
    return new MenuBar(this.topPageEl);
  }
}
