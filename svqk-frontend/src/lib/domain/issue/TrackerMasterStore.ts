import { readable } from 'svelte/store';
import { provide } from 'inversify-binding-decorators';
import { TYPES } from '$lib/arch/di/Types';
import { MasterStoreBase } from '$lib/arch/master/MasterStoreBase';
import type { TrackerModel } from '$lib/arch/api/Api';

type Fetch = typeof fetch;

export let trackers = readable([] as TrackerModel[]);

@provide(TYPES.MasterStore)
export class TrackerMasterStore extends MasterStoreBase<TrackerModel[]> {
  constructor() {
    super((api) => api.tracker.getAllTrackers());
  }

  override async load(fetch: Fetch) {
    await super.load(fetch);
    trackers = this.store;
  }
}
