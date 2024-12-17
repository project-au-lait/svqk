import type { IssueModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const issue = (await ApiHandler.handle<IssueModel>(fetch, (api) =>
    api.issueId.issuesDetail(Number(params.issueId))
  ))!;

  return {
    title: issue.subject,
    issue
  };
};
