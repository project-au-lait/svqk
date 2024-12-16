import { readable } from 'svelte/store';
import { provide } from 'inversify-binding-decorators';
import { TYPES } from '$lib/arch/di/Types';
import { MasterStoreBase } from '$lib/arch/master/MasterStoreBase';
import type { IssueStatusModel } from '$lib/arch/api/Api';

type Fetch = typeof fetch;

export let issueStatuses = readable([] as IssueStatusModel[]);

@provide(TYPES.MasterStore)
export class IssueStatusMasterStore extends MasterStoreBase<IssueStatusModel[]> {
  constructor() {
    super((api) => api.issueStatusesList());
  }

  override async load(fetch: Fetch) {
    await super.load(fetch);
    issueStatuses = this.store;
  }
}
