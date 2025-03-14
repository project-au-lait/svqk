import { DryRun } from '@arch/DryRun';

export default class BaseFacade {
  constructor(private readonly dryRun: DryRun) {}

  protected logStart(startLabel: string) {
    this.dryRun.logStr(`Start : ${startLabel}`);
  }
}
