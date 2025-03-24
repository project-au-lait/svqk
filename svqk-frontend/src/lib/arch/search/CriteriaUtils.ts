import rison from 'rison';

export default class CriteriaUtils {
  private static readonly PARAM_KEY_CRITERIA = 'c';
  private static readonly PARAM_KEY_OPTION = 'o';

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static encode(criteria: any, option?: any): string {
    const criteriaString = `?${this.PARAM_KEY_CRITERIA}=${this.encodeObj(criteria)}`;

    if (!option) {
      return criteriaString;
    }

    return criteriaString + `&${this.PARAM_KEY_OPTION}=${this.encodeObj(option)}`;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static decode(url: URL): any {
    const criteria = this.decodeObj(url, this.PARAM_KEY_CRITERIA);

    if (!criteria.pageControl) {
      criteria.pageControl = {
        pageNumber: 1
      };
    }

    return criteria;
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  static decodeOption(url: URL): any {
    return this.decodeObj(url, this.PARAM_KEY_OPTION);
  }

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  private static encodeObj(obj: any) {
    return rison.encode(obj);
  }

  private static decodeObj(url: URL, key: string) {
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    return rison.decode<any>(url.searchParams.get(key) ?? '()');
  }
}
