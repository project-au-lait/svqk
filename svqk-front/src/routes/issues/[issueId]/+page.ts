import type { IssueModel, IssueStatusModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const isNew = params.issueId == 'new';

  let issue = { issueStatus: {} as IssueStatusModel } as IssueModel;

  if (!isNew) {
    issue = (await ApiHandler.handle<IssueModel>(fetch, (api) =>
      api.issues.issuesDetail(params.issueId)
    ))!;
  }

  const issueStatuses = (await ApiHandler.handle<IssueStatusModel[]>(fetch, (api) =>
    api.issueStatuses.issueStatusesList()
  ))!;

  return {
    issue,
    issueStatuses,
    isNew
  };
};
