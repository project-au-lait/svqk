import * as m from '$lib/paraglide/messages';
import type { PageLoad } from "./$types";

export const load: PageLoad = async () => {
  return {
    title: m.register(),
  };
};
