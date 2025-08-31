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
    try {
      const response = await handler(api);
      return response.data || (response.text() as D);
    } catch (error) {
      if (error instanceof Response) {
        messageStore.show(error.statusText);
      } else {
        console.log(error);
      }
    }
  }
}
