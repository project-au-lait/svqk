<%_
idFields = compIdFields ?? [idField];

apiCallArgs = idFields.map((idField) => idField.javaType == "Integer" ? `Number(params.${idField.fieldName})` : `params.${idField.fieldName}`).join(", ");
%>
import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const <%= entityNmCamel %> = (await ApiHandler.handle<<%= entityNmPascal %>Model>(fetch, (api) =>
    api.<%= entityNmCamel %>.<%= entityNmCamel %>Detail(<%= apiCallArgs%>)
  ))!;

  return {
    title: `<%= idPath %>`,
    <%= entityNmCamel %>
  };
};
