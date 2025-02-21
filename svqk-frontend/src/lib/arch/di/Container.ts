import { Container } from 'inversify';
import { buildProviderModule } from 'inversify-binding-decorators';

export class DIContainer {
  private static instance: Container;

  private constructor() {}

  public static getInstance(): Container {
    if (!DIContainer.instance) {
      DIContainer.instance = new Container();
      DIContainer.instance.load(buildProviderModule());
    }
    return DIContainer.instance;
  }
}
