import fs from 'fs';
import { format } from 'date-fns';

export enum Action {
  GOTO = '遷移',
  CLICK = 'クリック',
  INPUT = '入力',
  EXPECT_VISIBLE = '表示されることを確認',
  EXPECT_TEXT = '値の一致を確認',
  NONE = ''
}

interface LogFormatter {
  format(pageName: string, itemName: string, action: Action, input?: string): string;

  get fileExt(): string;
}

class SpecLogFormatter implements LogFormatter {
  get fileExt(): string {
    return 'txt';
  }

  format(pageName: string, itemName: string, action: Action, input?: string): string {
    let log = pageName !== '' ? `${pageName}で` : '';

    switch (action) {
      case Action.GOTO:
        log += `「${itemName}」に${Action.GOTO}する。`;
        break;
      case Action.CLICK:
        log += `「${itemName}」を${Action.CLICK}する。`;
        break;
      case Action.INPUT:
        log += `「${itemName}」に「${input}」を${Action.INPUT}する。`;
        break;
      case Action.EXPECT_VISIBLE:
        log += !input
          ? `「${itemName}」が${Action.EXPECT_VISIBLE}する。`
          : `「${itemName}」に「${input}」が${Action.EXPECT_VISIBLE}する。`;
        break;
      case Action.EXPECT_TEXT:
        log += !input
          ? `「${itemName}」が正しいことを確認する。`
          : `「${itemName}」が「${input}」であることを確認する。`;
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

  format(pageName: string, itemName: string, action: Action, input?: string): string {
    const csvColumns = [pageName, itemName, action, action !== Action.NONE ? '' : input];

    return `${csvColumns.map((csvColumn) => csvColumn ?? '').filter((column) => column !== '')}`;
  }
}

export class DryRun {
  private filePath: string;

  private readonly LOG_DIR = 'dry_run_logs';

  private constructor(public isOn: boolean, private formatter: LogFormatter) {
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

  log(pageName: string, itemName: string, action: Action, input?: string) {
    const logStr = this.formatter.format(pageName, itemName, action, input);

    if (this.isOn) {
      fs.appendFileSync(this.filePath, logStr + '\n');
    }

    return logStr;
  }
}

const formatter =
  process.env.DRY_RUN_LOG === 'csv' ? new CsvLogFormatter() : new SpecLogFormatter();
const dryRunIsOn = process.env.DRY_RUN === 'true';
