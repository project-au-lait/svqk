import { t } from "$lib/translations";
import type { PageLoad } from "./$types";

export const load: PageLoad = async () => {
  return {
    title: t.get("msg.register"),
  };
};
