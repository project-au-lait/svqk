import type { IssueSearchCriteriaModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import CriteriaUtils from '$lib/arch/search/CriteriaUtils';
import { t } from '$lib/translations';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const criteria = {
    issueStatuses: [],
    ...CriteriaUtils.decode(url)
  } as IssueSearchCriteriaModel; // <.>

  let { open } = CriteriaUtils.decodeOption(url);
  open = open ?? false;

  const result =
    (await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearch(criteria)
    )) || ({} as IssueSearchResultModel); // <.>

  return {
    title: t.get('msg.issue'),
    criteria,
    open,
    result
  };
};
