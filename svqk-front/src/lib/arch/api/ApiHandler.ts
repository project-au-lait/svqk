import { env } from '$env/dynamic/public';
import { messageStore } from '$lib/arch/global/MessageStore';
import { Api, type HttpResponse } from './Api';

export default class ApiHandler {
  static getApi(
    fetch: (input: RequestInfo | URL, init?: RequestInit | undefined) => Promise<Response>
  ): Api<unknown> {
    const baseUrl = env.PUBLIC_BACKEND_URL || new URL(window.location.href).origin;

    return new Api({
      baseUrl,
      customFetch: fetch
    });
  }

  static async handle<D>(
    fetch: (input: RequestInfo | URL, init?: RequestInit | undefined) => Promise<Response>,
    handler: (api: Api<unknown>) => Promise<HttpResponse<D, unknown>>
  ): Promise<D | undefined> {
    const api = this.getApi(fetch);

    // TODO show loading

    const response = await handler(api);

    if (response.ok) {
      return response.data || (response.text() as D);
    } else {
      // TODO error handling
      messageStore.show(response.statusText);
      return undefined;
    }
  }
}
