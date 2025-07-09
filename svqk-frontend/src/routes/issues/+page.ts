import type { IssueSearchCriteriaModel, IssueSearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import CriteriaUtils from '$lib/arch/search/CriteriaUtils';
import * as m from '$lib/paraglide/messages';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  // <.>
  const criteria = {
    issueStatuses: [],
    ...CriteriaUtils.decode(url) // <.>
  } as IssueSearchCriteriaModel;

  const open = CriteriaUtils.decodeParam<boolean>(url, 'open') ?? false; // <.>

  const result =
    (await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.search(criteria)
    )) || ({} as IssueSearchResultModel); // <.>

  return {
    title: m.issue(),
    criteria,
    open,
    result
  };
};
