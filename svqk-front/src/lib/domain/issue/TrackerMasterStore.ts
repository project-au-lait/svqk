import { readable } from 'svelte/store';
import { provide } from 'inversify-binding-decorators';
import { TYPES } from '$lib/arch/di/types';
import { MasterStoreBase } from '$lib/arch/MasterStoreBase';
import type { TrackerModel } from '$lib/arch/api/Api';

type Fetch = typeof fetch;

export let trackers = readable([] as TrackerModel[]);

@provide(TYPES.MasterStore)
export class TrackerMasterStore extends MasterStoreBase<TrackerModel[]> {
  constructor() {
    super((api) => api.tracker.trackerList());
  }

  override async load(fetch: Fetch) {
    await super.load(fetch);
    trackers = this.store;
  }
}
