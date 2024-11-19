import type { IssueSearchConditionModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import { t } from '$lib/translations';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch }) => {
  const condition = { issueStatuses: [], pageNumber: 1 } as IssueSearchConditionModel; // <.>

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
