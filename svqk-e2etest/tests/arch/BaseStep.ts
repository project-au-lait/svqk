import { Action, DryRun } from '../arch/DryRun';

export default class BaseStep {
  constructor(private dryRun: DryRun) {}

  protected startStep(stepName: string) {
    this.dryRun.logStr(`Step : ${stepName}`);
  }
}
