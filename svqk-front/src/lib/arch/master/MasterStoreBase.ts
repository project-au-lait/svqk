import { readable } from 'svelte/store';
import { provide } from 'inversify-binding-decorators';
import { TYPES } from '$lib/arch/di/Types';
import type { Api, HttpResponse } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';

type Fetch = typeof fetch;
type ApiCall<T> = (api: Api<unknown>) => Promise<HttpResponse<T, unknown>>;

@provide(TYPES.MasterStore)
export class MasterStoreBase<T> {
  protected store = readable([] as T);
  private readonly apiCall: ApiCall<T>;

  constructor(apiCall: ApiCall<T>) {
    this.apiCall = apiCall;
  }

  async load(fetch: Fetch) {
    if (!this.apiCall) return;

    const response = (await ApiHandler.handle<T>(fetch, this.apiCall))!;

    this.store = readable(response);
  }
}
