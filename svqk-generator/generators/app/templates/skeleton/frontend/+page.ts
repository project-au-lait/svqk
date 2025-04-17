import type { PageLoad } from './$types';
import { env } from '$env/dynamic/public';

export const load: PageLoad = async ({ fetch }) => {
  <%_
  const id = compIdFields
    ? [
        '1',
        '1',
        'true',
        '2025-03-31',
        '2025-03-31T00:00:00',
        'Hello World'
      ].map(encodeURIComponent).join('/')
    : '1';
  -%>      
  const response = await fetch(`${env.PUBLIC_BACKEND_URL}/api/<%= entityNmCamel %>/<%= id %>`);
  const <%= entityNmCamel %> = await response.json();

  return {
    title: '<%= entityNmPascal %> Entity',
    status: response.status,
    <%= entityNmCamel %>
  };
};
