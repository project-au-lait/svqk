import { Fs, DestPaths, TemplateData } from "../types.js";
import { FileEditor } from "./FileEditor.js";

export class MenuEditor {
  fileEditor: FileEditor | null = null;

  constructor(
    private readonly fs: Fs,
    private readonly templateType: string,
    private readonly destPaths: DestPaths
  ) {
    this.fileEditor = new FileEditor(this.fs);
  }

  public update_menu(templateData: TemplateData) {
    if (this.templateType === "skeleton") {
      return;
    }

    this.fileEditor?.insert_snippet(
      "generators/app/templates/arch/frontend/routes/+layout_LINK.ejs",
      `${this.destPaths.destFrontPath}/routes/+layout.svelte`,
      "LINK",
      templateData,
      `href="/${templateData.entityNmPlural}"`
    );
  }

  public update_menu_test(templateData: TemplateData) {
    if (this.templateType === "skeleton") {
      return;
    }

    const menuBarTemplatePath =
      "generators/app/templates/arch/e2etest/pages/menu-bar";
    const menuBarDestPath = `${this.destPaths.destE2EPath}/pages/menu-bar`;

    this.fileEditor?.insert_snippet(
      `${menuBarTemplatePath}/MenuBar_GOTO.ejs`,
      `${menuBarDestPath}/MenuBar.ts`,
      "GOTO",
      templateData,
      `goto${templateData.entityNmPascal}ListPage`
    );
    this.fileEditor?.insert_snippet(
      `${menuBarTemplatePath}/MenuBar_IMPORT.ejs`,
      `${menuBarDestPath}/MenuBar.ts`,
      "IMPORT",
      templateData,
      `import ${templateData.entityNmPascal}ListPage`
    );
    this.fileEditor?.insert_snippet(
      `${menuBarTemplatePath}/MenuBarPageElement_CLICK.ejs`,
      `${menuBarDestPath}/MenuBarPageElement.ts`,
      "CLICK",
      templateData,
      `click${templateData.entityNmPascal}Link`
    );
  }
}
