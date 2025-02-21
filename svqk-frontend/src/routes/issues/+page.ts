import type { IssueSearchCriteriaModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import { t } from '$lib/translations';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const condition = {
    issueStatuses: [],
    pageControl: {
      pageNumber: 1
    },
    ...JSON.parse(decodeURIComponent(url.searchParams.get('q') ?? '{}'))
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
