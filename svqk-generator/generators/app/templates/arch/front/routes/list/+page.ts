import type { <%= entityNmPascal %>SearchCriteriaModel, <%= entityNmPascal %>SearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const criteria = {
    ...CriteriaUtils.decode(url)
  } as <%= entityNmPascal %>SearchCriteriaModel;

  const open = CriteriaUtils.decodeParam<boolean>(url, 'open') ?? false;

  const result =
    (await ApiHandler.handle<<%= entityNmPascal %>SearchResultModel>(fetch, (api) =>
      api.<%= entityNmCamel %>.<%= entityNmCamel %>Search(criteria)
    )) || ({} as <%= entityNmPascal %>SearchResultModel);

  return {
    criteria,
    open,
    result
  };
};
