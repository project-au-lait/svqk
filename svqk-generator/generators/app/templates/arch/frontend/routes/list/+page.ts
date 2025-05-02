import type { <%= entityNmPascal %>SearchCriteriaModel, <%= entityNmPascal %>SearchResultModel } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import CriteriaUtils from '$lib/arch/search/CriteriaUtils';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, url }) => {
  const criteria = {
    ...CriteriaUtils.decode(url)
  } as <%= entityNmPascal %>SearchCriteriaModel;

  const result =
    (await ApiHandler.handle<<%= entityNmPascal %>SearchResultModel>(fetch, (api) =>
      api.<%= entityNmCamel %>.search(criteria)
    )) || ({} as <%= entityNmPascal %>SearchResultModel);

  return {
    criteria,
    result
  };
};
