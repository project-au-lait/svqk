import { SnippetInsertionTarget, DestPaths, TemplateData } from "../types.js";
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

    const insertionTargetList: SnippetInsertionTarget[] = [];
    const menuBarTemplatePath =
      "generators/app/templates/arch/e2etest/pages/menu-bar";
    const menuBarDestPath = `${this.destPaths.destE2EPath}/pages/menu-bar`;

    insertionTargetList.push(
      {
        templatePath: `${menuBarTemplatePath}/MenuBar_GOTO.ejs`,
        destinationPath: `${menuBarDestPath}/MenuBar.ts`,
        placeholder: "GOTO",
        templateData: templateData,
        checkString: `goto${templateData.entityNmPascal}ListPage`,
      },
      {
        templatePath: `${menuBarTemplatePath}/MenuBar_IMPORT.ejs`,
        destinationPath: `${menuBarDestPath}/MenuBar.ts`,
        placeholder: "IMPORT",
        templateData: templateData,
        checkString: `import ${templateData.entityNmPascal}ListPage`,
      },
      {
        templatePath: `${menuBarTemplatePath}/MenuBarPageElement_CLICK.ejs`,
        destinationPath: `${menuBarDestPath}/MenuBarPageElement.ts`,
        placeholder: "CLICK",
        templateData: templateData,
        checkString: `click${templateData.entityNmPascal}Link`,
      },
      {
        templatePath:
          "generators/app/templates/arch/front/routes/+layout_LINK.ejs",
        destinationPath: `${this.destPaths.destFrontPath}/routes/+layout.svelte`,
        placeholder: "LINK",
        templateData: templateData,
        checkString: `href="/${templateData.entityNmPlural}"`,
      }
    );

    this.fileEditer?.insert_snippet(insertionTargetList);
  }
}
