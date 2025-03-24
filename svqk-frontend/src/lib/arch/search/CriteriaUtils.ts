import rison from 'rison';

export default class CriteriaUtils {
  private static readonly PARAM_KEY_CRITERIA = 'c';

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static encode(criteria: any, param?: { [key: string]: any }): string {
    const criteriaString = `?${this.PARAM_KEY_CRITERIA}=${this.encodeObj(criteria)}`;

    if (!param) {
      return criteriaString;
    }

    const paramStr = Object.entries(param)
      .map(([key, value]) => `${key}=${this.encodeObj(value)}`)
      .join('&');

    return criteriaString + `&${paramStr}`;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static decode(url: URL): any {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    const criteria = this.decodeParam<any>(url, this.PARAM_KEY_CRITERIA) ?? {};

    if (!criteria.pageControl) {
      criteria.pageControl = {
        pageNumber: 1
      };
    }

    return criteria;
  }

  static decodeParam<T>(url: URL, key: string) {
    const param = url.searchParams.get(key);

    if (param) {
      return rison.decode<T>(param);
    }
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private static encodeObj(obj: any) {
    return rison.encode(obj);
  }
}
