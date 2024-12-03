import { multiInject } from 'inversify';
import { provide } from 'inversify-binding-decorators';
import { TYPES } from '$lib/arch/di/Types';
import { MasterStoreBase } from '$lib/arch/master/MasterStoreBase';

type Fetch = typeof fetch;

@provide(TYPES.LoadExecutor)
export class LoadExecutor {
  constructor(
    @multiInject(TYPES.MasterStore) private readonly stores: MasterStoreBase<unknown>[]
  ) {}

  async loadAll(fetch: Fetch) {
    await Promise.all(this.stores.map((m) => m.load(fetch)));
  }
}
