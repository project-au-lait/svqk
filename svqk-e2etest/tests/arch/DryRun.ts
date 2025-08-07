import fs from 'fs';
import { format } from 'date-fns';
import * as m from '@paraglide/messages';
import { getLocale } from '@paraglide/runtime.js';

export enum Action {
  GOTO,
  CLICK,
  INPUT,
  EXPECT_VISIBLE,
  EXPECT_TEXT,
  NONE
}

interface LogFormatter {
  format(pageNameKey: string, itemNameKey: string, action: Action, input?: string): string;

  get fileExt(): string;
}

class SpecLogFormatter implements LogFormatter {
  get fileExt(): string {
    return 'txt';
  }

  format(pageName: string, itemName: string, action: Action, input?: string): string {
    return getLocale() == 'ja'
      ? this.format_ja(pageName, itemName, action, input)
      : this.format_en(pageName, itemName, action, input);
  }

  format_en(pageName: string, itemName: string, action: Action, input?: string): string {
    let log = pageName !== '' ? `on the ${pageName} page.` : '';

    switch (action) {
      case Action.GOTO:
        log = `Navigate to the '${itemName}' ${log}`;
        break;
      case Action.CLICK:
        log = `Click '${itemName}' ${log}`;
        break;
      case Action.INPUT:
        log = `Input '${input}' to '${itemName}' ${log}`;
        break;
      case Action.EXPECT_VISIBLE:
        log = !input
          ? `Confirm that '${itemName}' is displayed ${log}`
          : `Confirm that '${input}' is displayed in '${itemName}' ${log}`;
        break;
      case Action.EXPECT_TEXT:
        log = `Confirm that '${itemName}' is '${input}' ${log}`;
        break;
      case Action.NONE:
        log += input;
        break;
    }

    return log;
  }

  format_ja(pageName: string, itemName: string, action: Action, input?: string): string {
    let log = pageName !== '' ? `${pageName}画面で` : '';

    switch (action) {
      case Action.GOTO:
        log += `「${itemName}」に遷移する。`;
        break;
      case Action.CLICK:
        log += `「${itemName}」をクリックする。`;
        break;
      case Action.INPUT:
        log += `「${itemName}」に「${input}」を入力する。`;
        break;
      case Action.EXPECT_VISIBLE:
        log += !input
          ? `「${itemName}」が表示されることを確認する。`
          : `「${itemName}」に「${input}」が表示されることを確認する。`;
        break;
      case Action.EXPECT_TEXT:
        log += `「${itemName}」が「${input}」であることを確認する。`;
        break;
      case Action.NONE:
        log += input;
        break;
    }

    return log;
  }
}

class CsvLogFormatter implements LogFormatter {
  get fileExt(): string {
    return 'csv';
  }

  format(pageNameKey: string, itemNameKey: string, action: Action, input?: string): string {
    const csvColumns = [pageNameKey, itemNameKey, action, action !== Action.NONE ? '' : input];

    return `${csvColumns.map((csvColumn) => csvColumn ?? '').filter((column) => column !== '')}`;
  }
}

export class DryRun {
  private readonly filePath: string;

  private readonly LOG_DIR = 'dry_run_logs';

  private constructor(public isOn: boolean, private readonly formatter: LogFormatter) {
    if (isOn) {
      this.createDir();
      this.filePath = this.buildFilePath(formatter);
      console.log(`Dry run is on. Log is written to ${this.filePath}`);
    } else {
      this.filePath = '';
    }
  }

  static build() {
    const formatter =
      process.env.DRY_RUN_LOG === 'csv' ? new CsvLogFormatter() : new SpecLogFormatter();
    const dryRunIsOn = process.env.DRY_RUN === 'true';

    return new DryRun(dryRunIsOn, formatter);
  }

  private createDir() {
    if (!fs.existsSync(this.LOG_DIR)) {
      fs.mkdirSync(this.LOG_DIR);
    }
  }

  private buildFilePath(formatter: LogFormatter) {
    return `${this.LOG_DIR}/dryrun_${format(new Date(), 'yyyy-MM-dd_HHmmss')}.${formatter.fileExt}`;
  }

  log(pageNameKey: string, itemNameKey: string, action: Action, input?: string) {
    const pageName = this.resolveName(pageNameKey);
    const itemName = this.resolveName(itemNameKey);

    const logStr = this.formatter.format(pageName, itemName, action, input);

    this.logStr(logStr);

    return logStr;
  }

  logStr(str: string) {
    if (this.isOn) {
      fs.appendFileSync(this.filePath, str + '\n');
    }
  }

  resolveName(nameKey: string) {
    const normalizedKey = nameKey.startsWith('#') ? nameKey.slice(1) : nameKey;

    const translateFn = (m as Record<string, any>)[normalizedKey];

    if (typeof translateFn === 'function') {
      return translateFn();
    }

    return normalizedKey;
  }
}
