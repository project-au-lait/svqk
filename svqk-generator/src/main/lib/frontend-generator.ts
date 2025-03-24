import { TemplateData } from "../types.js";

export class FrontendGenerator {
  private readonly fs: {
    copyTpl: (src: string, dest: string, data: TemplateData) => void;
  };
  private readonly templatePath: (...pathSegments: string[]) => string;
  private readonly destinationPath: (...pathSegments: string[]) => string;

  constructor({
    fs,
    templatePath,
    destinationPath,
  }: {
    fs: { copyTpl: (src: string, dest: string, data: TemplateData) => void };
    templatePath: (...pathSegments: string[]) => string;
    destinationPath: (...pathSegments: string[]) => string;
  }) {
    this.fs = fs;
    this.templatePath = templatePath;
    this.destinationPath = destinationPath;
  }

  public generate_frontend(
    tmplData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    let pathPairs: [string, string][] = [];

    const entityPathCamel = `${destinationPath}/routes/${tmplData.entityNmCamel}`;
    const entityPathPlural = `${destinationPath}/routes/${tmplData.entityNmPlural}`;
    const forntendPagePath =
      FrontendGenerator.build_frontend_page_path(tmplData);

    if (templateType === "skeleton") {
      pathPairs = [
        ["front/+page.svelte", `${entityPathCamel}/+page.svelte`],
        ["front/+page.ts", `${entityPathCamel}/+page.ts`],
      ];
    } else if (templateType === "arch") {
      pathPairs = [
        // For list page
        ["front/routes/list/+page.svelte", `${entityPathPlural}/+page.svelte`],
        ["front/routes/list/+page.ts", `${entityPathPlural}/+page.ts`],

        // For create page
        [
          "front/routes/new/+page.svelte",
          `${entityPathPlural}/new/+page.svelte`,
        ],
        ["front/routes/new/+page.ts", `${entityPathPlural}/new/+page.ts`],
        [
          "front/lib/Form.svelte",
          `${destinationPath}/lib/domain/${tmplData.entityNmPlural}/${tmplData.entityNmPascal}Form.svelte`,
        ],

        // For update page
        [
          "front/routes/entityId/+page.svelte",
          `${entityPathPlural}/${forntendPagePath}/+page.svelte`,
        ],
        [
          "front/routes/entityId/+page.ts",
          `${entityPathPlural}/${forntendPagePath}/+page.ts`,
        ],
      ];
    }

    for (const [srcPath, destPath] of pathPairs) {
      this.output_file(srcPath, destPath, tmplData, templateType);
    }
  }

  public generate_e2etest(
    tmplData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    let pathPairs: [string, string][] = [];

    const inputTmplPath = "e2etest/pages/input";
    const listTmplPath = "e2etest/pages/list";
    const inputDestPath = `${destinationPath}/pages/${tmplData.entityNmKebab}-input`;
    const listDestPath = `${destinationPath}/pages/${tmplData.entityNmKebab}-list`;

    if (templateType === "skeleton") {
      pathPairs = [
        [
          "e2etest/spec.ts",
          `${destinationPath}/${FrontendGenerator.build_e2e_spec_path(tmplData)}`,
        ],
      ];
    } else if (templateType === "arch") {
      pathPairs = [
        [
          "e2etest/spec.ts",
          `${destinationPath}/${FrontendGenerator.build_e2e_spec_path(tmplData)}`,
        ],
        [
          "e2etest/Facade.ts",
          `${destinationPath}/facades/${tmplData.entityNmPascal}Facade.ts`,
        ],
        [
          "e2etest/Factory.ts.ejs",
          `${destinationPath}/factories/${tmplData.entityNmPascal}Factory.ts`,
        ],
        [
          `${inputTmplPath}/InputPage.ts`,
          `${inputDestPath}/${tmplData.entityNmPascal}InputPage.ts`,
        ],
        [
          `${inputTmplPath}/InputPageElement.ts`,
          `${inputDestPath}/${tmplData.entityNmPascal}InputPageElement.ts`,
        ],
        [
          `${listTmplPath}/ListPage.ts`,
          `${listDestPath}/${tmplData.entityNmPascal}ListPage.ts`,
        ],
        [
          `${listTmplPath}/ListPageElement.ts`,
          `${listDestPath}/${tmplData.entityNmPascal}ListPageElement.ts`,
        ],
      ];
    }

    for (const [srcPath, destPath] of pathPairs) {
      this.output_file(srcPath, destPath, tmplData, templateType);
    }
  }

  private static build_frontend_page_path(tmplData: TemplateData): string {
    const idFields = tmplData.compIdFields || [tmplData.idField];
    return idFields.map((idField) => `[${idField.fieldName}]`).join("/");
  }

  private static build_e2e_spec_path(tmplData: TemplateData): string {
    return `specs/${tmplData.domainPkgNm.split(".").slice(-1)[0]}/${tmplData.entityNmCamel}.spec.ts`;
  }

  private output_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData,
    templateType: string
  ) {
    this.fs.copyTpl(
      this.templatePath(`${templateType}/${tmplPath}`),
      this.destinationPath(destinationPath),
      tmplData
    );
  }
}
