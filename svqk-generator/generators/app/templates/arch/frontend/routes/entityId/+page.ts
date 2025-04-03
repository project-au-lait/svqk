<%_
idFields = compIdFields ?? [idField];

const convertParam = (field) => {
  if (field.javaType === "Integer") {
    return `Number(params.${field.fieldName})`;
  } else if (field.javaType === "Boolean") {
    return `params.${field.fieldName} === 'true'`;
  } else {
    return `params.${field.fieldName}`;
  }
};

apiCallArgs = idFields.map(convertParam).join(", ");
%>
import type { <%= entityNmPascal %>Model } from '$lib/arch/api/Api';
import ApiHandler from '$lib/arch/api/ApiHandler';
import type { PageLoad } from './$types';

export const load: PageLoad = async ({ fetch, params }) => {
  const <%= entityNmCamel %> = (await ApiHandler.handle<<%= entityNmPascal %>Model>(fetch, (api) =>
    api.<%= entityNmCamel %>.<%= entityNmCamel %>Detail(<%- apiCallArgs %>)
  ))!;

  return {
    title: `<%= idPath %>`,
    <%= entityNmCamel %>
  };
};
