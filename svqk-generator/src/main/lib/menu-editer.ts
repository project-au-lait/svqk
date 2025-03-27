import { DestPaths, TemplateData } from "../types.js";
import { FileEditer } from "./file-editer.js";

export class MenuEditer {
  fileEditer: FileEditer | null = null;

  constructor(
    private readonly fs: {
      copyTpl: (src: string, dest: string, data: TemplateData) => void;
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      copy: (src: string, dest: string, options?: any) => void;
      delete: (filepath: string) => void;
      read: (filepath: string) => string | null;
    },
    private readonly templateType: string,
    private readonly destPaths: DestPaths
  ) {
    this.fileEditer = new FileEditer(this.fs);
  }

  public update_menu(templateData: TemplateData) {
    if (this.templateType === "skeleton") {
      return [];
    }

    const menuBarTemplatePath =
      "generators/app/templates/arch/e2etest/pages/menu-bar";
    const menuBarDestPath = `${this.destPaths.destE2EPath}/pages/menu-bar`;

    this.fileEditer?.insert_snippet(
      `${menuBarTemplatePath}/MenuBar_GOTO.ejs`,
      `${menuBarDestPath}/MenuBar.ts`,
      "GOTO",
      templateData,
      `goto${templateData.entityNmPascal}ListPage`
    );
    this.fileEditer?.insert_snippet(
      `${menuBarTemplatePath}/MenuBar_IMPORT.ejs`,
      `${menuBarDestPath}/MenuBar.ts`,
      "IMPORT",
      templateData,
      `import ${templateData.entityNmPascal}ListPage`
    );
    this.fileEditer?.insert_snippet(
      `${menuBarTemplatePath}/MenuBarPageElement_CLICK.ejs`,
      `${menuBarDestPath}/MenuBarPageElement.ts`,
      "CLICK",
      templateData,
      `click${templateData.entityNmPascal}Link`
    );
    this.fileEditer?.insert_snippet(
      "generators/app/templates/arch/front/routes/+layout_LINK.ejs",
      `${this.destPaths.destFrontPath}/routes/+layout.svelte`,
      "LINK",
      templateData,
      `href="/${templateData.entityNmPlural}"`
    );
  }
}
