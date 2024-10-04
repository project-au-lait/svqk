/* eslint-disable */
/* tslint:disable */
/*
 * ---------------------------------------------------------------
 * ## THIS FILE WAS GENERATED VIA SWAGGER-TYPESCRIPT-API        ##
 * ##                                                           ##
 * ## AUTHOR: acacode                                           ##
 * ## SOURCE: https://github.com/acacode/swagger-typescript-api ##
 * ---------------------------------------------------------------
 */

export interface HelloModel {
  /** @format int32 */
  id: number;
  message?: string;
}

export interface IdModel {
  /** @format int32 */
  id?: number;
}

export interface IssueModel {
  /** @format int32 */
  id: number;
  /** @pattern \S */
  subject: string;
  description?: string;
  dueDate?: LocalDate;
  issueStatus: IssueStatusModel;
  tracker: TrackerModel;
  /** @format int64 */
  version: number;
  updatedAt: LocalDateTime;
}

export interface IssueSearchConditionModel {
  /** @format int32 */
  pageNumber?: number;
  /** @format int32 */
  pageSize?: number;
  sortOrders?: SortOrderModel[];
  text?: string;
  issueStatuses?: IssueStatusModel[];
  dueDate?: string;
  subjectOnly?: boolean;
}

export interface IssueSearchResultModel {
  list: IssueModel[];
  /** @format int64 */
  count: number;
  /** @format int32 */
  limit: number;
  /** @format int32 */
  start: number;
  /** @format int64 */
  end: number;
  /** @format int32 */
  lastPage: number;
  pageNums: number[];
}

export interface IssueStatusModel {
  id: string;
  name: string;
  /** @format int64 */
  version: number;
}

/**
 * @format date
 * @example "2022-03-10T00:00:00.000Z"
 */
export type LocalDate = string;

/**
 * @format date-time
 * @example "2022-03-10T12:15:50.000Z"
 */
export type LocalDateTime = string;

export interface SortOrderModel {
  asc?: boolean;
  field?: string;
}

export interface TrackerModel {
  /** @format int32 */
  id: number;
  name: string;
}

export type QueryParamsType = Record<string | number, any>;
export type ResponseFormat = keyof Omit<Body, 'body' | 'bodyUsed'>;

export interface FullRequestParams extends Omit<RequestInit, 'body'> {
  /** set parameter to `true` for call `securityWorker` for this request */
  secure?: boolean;
  /** request path */
  path: string;
  /** content type of request body */
  type?: ContentType;
  /** query params */
  query?: QueryParamsType;
  /** format of response (i.e. response.json() -> format: "json") */
  format?: ResponseFormat;
  /** request body */
  body?: unknown;
  /** base url */
  baseUrl?: string;
  /** request cancellation token */
  cancelToken?: CancelToken;
}

export type RequestParams = Omit<FullRequestParams, 'body' | 'method' | 'query' | 'path'>;

export interface ApiConfig<SecurityDataType = unknown> {
  baseUrl?: string;
  baseApiParams?: Omit<RequestParams, 'baseUrl' | 'cancelToken' | 'signal'>;
  securityWorker?: (
    securityData: SecurityDataType | null
  ) => Promise<RequestParams | void> | RequestParams | void;
  customFetch?: typeof fetch;
}

export interface HttpResponse<D extends unknown, E extends unknown = unknown> extends Response {
  data: D;
  error: E;
}

type CancelToken = Symbol | string | number;

export enum ContentType {
  Json = 'application/json',
  FormData = 'multipart/form-data',
  UrlEncoded = 'application/x-www-form-urlencoded',
  Text = 'text/plain'
}

export class HttpClient<SecurityDataType = unknown> {
  public baseUrl: string = '';
  private securityData: SecurityDataType | null = null;
  private securityWorker?: ApiConfig<SecurityDataType>['securityWorker'];
  private abortControllers = new Map<CancelToken, AbortController>();
  private customFetch = (...fetchParams: Parameters<typeof fetch>) => fetch(...fetchParams);

  private baseApiParams: RequestParams = {
    credentials: 'same-origin',
    headers: {},
    redirect: 'follow',
    referrerPolicy: 'no-referrer'
  };

  constructor(apiConfig: ApiConfig<SecurityDataType> = {}) {
    Object.assign(this, apiConfig);
  }

  public setSecurityData = (data: SecurityDataType | null) => {
    this.securityData = data;
  };

  protected encodeQueryParam(key: string, value: any) {
    const encodedKey = encodeURIComponent(key);
    return `${encodedKey}=${encodeURIComponent(typeof value === 'number' ? value : `${value}`)}`;
  }

  protected addQueryParam(query: QueryParamsType, key: string) {
    return this.encodeQueryParam(key, query[key]);
  }

  protected addArrayQueryParam(query: QueryParamsType, key: string) {
    const value = query[key];
    return value.map((v: any) => this.encodeQueryParam(key, v)).join('&');
  }

  protected toQueryString(rawQuery?: QueryParamsType): string {
    const query = rawQuery || {};
    const keys = Object.keys(query).filter((key) => 'undefined' !== typeof query[key]);
    return keys
      .map((key) =>
        Array.isArray(query[key])
          ? this.addArrayQueryParam(query, key)
          : this.addQueryParam(query, key)
      )
      .join('&');
  }

  protected addQueryParams(rawQuery?: QueryParamsType): string {
    const queryString = this.toQueryString(rawQuery);
    return queryString ? `?${queryString}` : '';
  }

  private contentFormatters: Record<ContentType, (input: any) => any> = {
    [ContentType.Json]: (input: any) =>
      input !== null && (typeof input === 'object' || typeof input === 'string')
        ? JSON.stringify(input)
        : input,
    [ContentType.Text]: (input: any) =>
      input !== null && typeof input !== 'string' ? JSON.stringify(input) : input,
    [ContentType.FormData]: (input: any) =>
      Object.keys(input || {}).reduce((formData, key) => {
        const property = input[key];
        formData.append(
          key,
          property instanceof Blob
            ? property
            : typeof property === 'object' && property !== null
            ? JSON.stringify(property)
            : `${property}`
        );
        return formData;
      }, new FormData()),
    [ContentType.UrlEncoded]: (input: any) => this.toQueryString(input)
  };

  protected mergeRequestParams(params1: RequestParams, params2?: RequestParams): RequestParams {
    return {
      ...this.baseApiParams,
      ...params1,
      ...(params2 || {}),
      headers: {
        ...(this.baseApiParams.headers || {}),
        ...(params1.headers || {}),
        ...((params2 && params2.headers) || {})
      }
    };
  }

  protected createAbortSignal = (cancelToken: CancelToken): AbortSignal | undefined => {
    if (this.abortControllers.has(cancelToken)) {
      const abortController = this.abortControllers.get(cancelToken);
      if (abortController) {
        return abortController.signal;
      }
      return void 0;
    }

    const abortController = new AbortController();
    this.abortControllers.set(cancelToken, abortController);
    return abortController.signal;
  };

  public abortRequest = (cancelToken: CancelToken) => {
    const abortController = this.abortControllers.get(cancelToken);

    if (abortController) {
      abortController.abort();
      this.abortControllers.delete(cancelToken);
    }
  };

  public request = async <T = any, E = any>({
    body,
    secure,
    path,
    type,
    query,
    format,
    baseUrl,
    cancelToken,
    ...params
  }: FullRequestParams): Promise<HttpResponse<T, E>> => {
    const secureParams =
      ((typeof secure === 'boolean' ? secure : this.baseApiParams.secure) &&
        this.securityWorker &&
        (await this.securityWorker(this.securityData))) ||
      {};
    const requestParams = this.mergeRequestParams(params, secureParams);
    const queryString = query && this.toQueryString(query);
    const payloadFormatter = this.contentFormatters[type || ContentType.Json];
    const responseFormat = format || requestParams.format;

    return this.customFetch(
      `${baseUrl || this.baseUrl || ''}${path}${queryString ? `?${queryString}` : ''}`,
      {
        ...requestParams,
        headers: {
          ...(requestParams.headers || {}),
          ...(type && type !== ContentType.FormData ? { 'Content-Type': type } : {})
        },
        signal: (cancelToken ? this.createAbortSignal(cancelToken) : requestParams.signal) || null,
        body: typeof body === 'undefined' || body === null ? null : payloadFormatter(body)
      }
    ).then(async (response) => {
      const r = response as HttpResponse<T, E>;
      r.data = null as unknown as T;
      r.error = null as unknown as E;

      const data = !responseFormat
        ? r
        : await response[responseFormat]()
            .then((data) => {
              if (r.ok) {
                r.data = data;
              } else {
                r.error = data;
              }
              return r;
            })
            .catch((e) => {
              r.error = e;
              return r;
            });

      if (cancelToken) {
        this.abortControllers.delete(cancelToken);
      }

      return data;
    });
  };
}

/**
 * @title svqk-back API
 * @version 0.7-SNAPSHOT
 */
export class Api<SecurityDataType extends unknown> extends HttpClient<SecurityDataType> {
  front = {
    /**
     * No description
     *
     * @tags Front Controller
     * @name FrontList
     * @request GET:/api/v1/front
     */
    frontList: (params: RequestParams = {}) =>
      this.request<LocalDate, any>({
        path: `/api/v1/front`,
        method: 'GET',
        ...params
      })
  };
  hello = {
    /**
     * No description
     *
     * @tags Hello Controller
     * @name HelloCreate
     * @request POST:/api/v1/hello
     */
    helloCreate: (data: HelloModel, params: RequestParams = {}) =>
      this.request<number, any>({
        path: `/api/v1/hello`,
        method: 'POST',
        body: data,
        type: ContentType.Json,
        ...params
      }),

    /**
     * No description
     *
     * @tags Hello Controller
     * @name HelloDetail
     * @request GET:/api/v1/hello/{id}
     */
    helloDetail: (id: number, params: RequestParams = {}) =>
      this.request<HelloModel, any>({
        path: `/api/v1/hello/${id}`,
        method: 'GET',
        format: 'json',
        ...params
      })
  };
  issueStatuses = {
    /**
     * No description
     *
     * @tags Issue Status Controller
     * @name IssueStatusesList
     * @request GET:/api/v1/issue-statuses
     */
    issueStatusesList: (params: RequestParams = {}) =>
      this.request<IssueStatusModel[], any>({
        path: `/api/v1/issue-statuses`,
        method: 'GET',
        format: 'json',
        ...params
      })
  };
  issues = {
    /**
     * No description
     *
     * @tags Issue Controller
     * @name IssuesCreate
     * @request POST:/api/v1/issues
     */
    issuesCreate: (data: IssueModel, params: RequestParams = {}) =>
      this.request<IdModel, any>({
        path: `/api/v1/issues`,
        method: 'POST',
        body: data,
        type: ContentType.Json,
        format: 'json',
        ...params
      }),

    /**
     * No description
     *
     * @tags Issue Controller
     * @name IssuesSearchCreate
     * @request POST:/api/v1/issues/search
     */
    issuesSearchCreate: (data: IssueSearchConditionModel, params: RequestParams = {}) =>
      this.request<IssueSearchResultModel, any>({
        path: `/api/v1/issues/search`,
        method: 'POST',
        body: data,
        type: ContentType.Json,
        format: 'json',
        ...params
      }),

    /**
     * No description
     *
     * @tags Issue Controller
     * @name IssuesDetail
     * @request GET:/api/v1/issues/{issueId}
     */
    issuesDetail: (issueId: number, params: RequestParams = {}) =>
      this.request<IssueModel, any>({
        path: `/api/v1/issues/${issueId}`,
        method: 'GET',
        format: 'json',
        ...params
      })
  };
  tracker = {
    /**
     * No description
     *
     * @tags Tracker Controller
     * @name TrackerList
     * @request GET:/api/v1/tracker
     */
    trackerList: (params: RequestParams = {}) =>
      this.request<TrackerModel[], any>({
        path: `/api/v1/tracker`,
        method: 'GET',
        format: 'json',
        ...params
      })
  };
}
