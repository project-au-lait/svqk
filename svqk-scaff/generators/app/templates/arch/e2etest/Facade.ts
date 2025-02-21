import BaseFacade from "../arch/BaseFacade";
import MenuBar from '../pages/menu-bar/MenuBar';

export class <%= entityNmPascal %>Facade extends BaseFacade {
  async reference<%= entityNmPascal %>ById(menuBar: MenuBar, id: string) {
    this.logStart("<%= entityNmPascal %> Reference");

    const <%= entityNmCamel %>ListPage = await menuBar.goto<%= entityNmPascal %>ListPage();
    const <%= entityNmCamel %>InputPage = await <%= entityNmCamel %>ListPage.goto<%= entityNmPascal %>ById(id);

    return <%= entityNmCamel %>InputPage;
  }
}
