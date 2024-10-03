import { DryRun } from './DryRun';

export default class BaseFacade {
  constructor(private dryRun: DryRun) {}

  protected logStart(startLabel: string) {
    this.dryRun.logStr(`Start : ${startLabel}`);
  }
}
