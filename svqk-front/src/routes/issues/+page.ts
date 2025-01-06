import type { IssueSearchCriteriaModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import { t } from '$lib/translations';
import qs from 'qs';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const condition = {
    issueStatuses: [],
    pageNumber: 1,
    ...qs.parse(url.searchParams.toString(), { allowEmptyArrays: true })
  } as IssueSearchCriteriaModel;

  const result =
    (await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearch(condition)
    )) || ({} as IssueSearchResultModel); // <.>

  return {
    title: t.get('msg.issue'),
    condition,
    result
  };
};
