import { goto } from '$app/navigation';
import BaseFacade from "../arch/BaseFacade";

export class <%= entityNmPascal %>Facade extends BaseFacade {
  async reference<%= entityNmPascal %>ById(id: string) {
    this.logStart("<%= entityNmPascal %> Reference");

    const <%= entityNmCamel %>ListPage = await goto(`/<%= entityNmPlural %>`);
    const <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>ListPage.goto<%= entityNmPascal %>ById(id);

    return <%= entityNmCamel %>InputPage;
  }
}
