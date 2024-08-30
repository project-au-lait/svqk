import type {
  IssueSearchConditionModel,
  IssueSearchResultModel,
  IssueStatusModel
} from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch }) => {
  const condition = { issueStatuses: [] } as IssueSearchConditionModel;

  const result =
    (await ApiHandler.handle<IssueSearchResultModel>(fetch, (api) =>
      api.issues.issuesSearchCreate(condition)
    )) || ({} as IssueSearchResultModel);

  const issueStatuses = (await ApiHandler.handle<IssueStatusModel[]>(fetch, (api) =>
    api.issueStatuses.issueStatusesList()
  ))!;

  return {
    condition,
    issueStatuses,
    result
  };
};
