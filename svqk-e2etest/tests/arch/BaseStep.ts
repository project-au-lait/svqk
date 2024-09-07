import { Action, DryRun } from '../arch/DryRun';

export default class BaseStep {
  constructor(private dryRun: DryRun) {}

  protected startStep(stepName: string) {
    if (this.dryRun.isOn) {
      this.dryRun.log('', '', Action.NONE, `${stepName}ステップ`);
    }
  }
}
