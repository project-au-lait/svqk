import type { IssueSearchCriteriaModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import { t } from '$lib/translations';
import type { PageLoad } from './$types';
import qs from 'qs';

export const load: PageLoad = async ({ fetch, url }) => {
  let condition = { issueStatuses: [], pageNumber: 1 } as IssueSearchCriteriaModel; // <.>
  condition = Object.assign(
    condition,
    qs.parse(url.searchParams.toString(), { allowEmptyArrays: true })
  );
  console.log(condition);

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
