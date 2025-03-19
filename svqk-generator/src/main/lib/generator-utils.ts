import pluralize from "pluralize";
import {
  Field,
  Metadata,
  MetadataConfig,
  TemplateData,
  GenerateTarget,
  SnippetInsertionTarget,
  DestPaths,
} from "../types.js";

export class GeneratorUtils {
  static async load_json_file(filePath: string) {
    try {
      const module = await import(filePath, {
        with: { type: "json" },
      });
      return module.default;
    } catch {
      return null;
    }
  }

  static build_generate_targets(
    metadataConfig: MetadataConfig,
    component: string,
    templateType: string,
    inputTables: string[],
    destPaths: DestPaths
  ): GenerateTarget[] {
    let methods: ((
      templateData: TemplateData,
      destPaths: DestPaths,
      templateType: string
    ) => GenerateTarget[])[] = [];

    switch (component) {
      case "backend":
        methods = [this.build_generate_targets_of_backend];
        break;
      case "integration-test":
        methods = [this.build_generate_targets_of_integrationtest];
        break;
      case "frontend":
        methods = [this.build_generate_targets_of_frontend];
        break;
      case "e2e-test":
        methods = [this.build_generate_targets_of_e2etest];
        break;
      case "all":
        methods = [
          this.build_generate_targets_of_backend,
          this.build_generate_targets_of_integrationtest,
          this.build_generate_targets_of_frontend,
          this.build_generate_targets_of_e2etest,
        ];
        break;
    }

    const generateTargets: GenerateTarget[] = [];

    metadataConfig.list.forEach((metaData) => {
      if (
        inputTables.includes(metaData.tableName) ||
        inputTables.includes(metaData.className)
      ) {
        const templateData = this.build_template_data(metaData, metadataConfig);

        methods.forEach((method) => {
          generateTargets.push(
            ...method(templateData, destPaths, templateType)
          );
        });
      }
    });

    return generateTargets;
  }

  static build_snippet_insert_targets(
    metadataConfig: MetadataConfig,
    component: string,
    templateType: string,
    inputTables: string[],
    destPaths: DestPaths
  ): SnippetInsertionTarget[] {
    if (templateType === "skeleton") {
      return [];
    }

    const insertionTargets: SnippetInsertionTarget[] = [];

    metadataConfig.list.forEach((metaData) => {
      if (
        inputTables.includes(metaData.tableName) ||
        inputTables.includes(metaData.className)
      ) {
        const templateData = this.build_template_data(metaData, metadataConfig);
        const menuBarTemplatePath =
          "generators/app/templates/arch/e2etest/pages/menu-bar";
        const menuBarDestPath = `${destPaths.destE2EPath}/pages/menu-bar`;

        if (component === "e2e-test" || component === "all") {
          insertionTargets.push(
            {
              templatePath: `${menuBarTemplatePath}/MenuBar_GOTO.ejs`,
              destinationPath: `${menuBarDestPath}/MenuBar.ts`,
              placeholder: "GOTO",
              templateData: templateData,
            },
            {
              templatePath: `${menuBarTemplatePath}/MenuBar_IMPORT.ejs`,
              destinationPath: `${menuBarDestPath}/MenuBar.ts`,
              placeholder: "IMPORT",
              templateData: templateData,
            },
            {
              templatePath: `${menuBarTemplatePath}/MenuBarPageElement_CLICK.ejs`,
              destinationPath: `${menuBarDestPath}/MenuBarPageElement.ts`,
              placeholder: "CLICK",
              templateData: templateData,
            },
            {
              templatePath:
                "generators/app/templates/arch/front/routes/+layout_LINK.ejs",
              destinationPath: `${destPaths.destFrontPath}/routes/+layout.svelte`,
              placeholder: "LINK",
              templateData: templateData,
            }
          );
        }
      }
    });

    return insertionTargets;
  }

  public static build_template_data(
    metadata: Metadata,
    metadataConfig: MetadataConfig
  ): TemplateData {
    const entityNmPascal = this.extract_entity_name(metadata.className);
    const idField = metadata.fields.find((field) => field.id) ?? ({} as Field);

    this.set_field_pascal_name(metadata.fields);

    return {
      domainPkgNm: metadata.packageName,
      interfacesPkgNm: metadata.packageName.replace(".domain.", ".interfaces."),
      entityNmPascal: entityNmPascal,
      entityNmCamel: this.pascal_to_camel(entityNmPascal),
      entityNmAllCaps: entityNmPascal.toUpperCase(),
      entityNmPlural: pluralize(entityNmPascal.toLowerCase()),
      entityNmKebab: this.pascal_to_kebab(entityNmPascal),
      fields: metadata.fields,
      idField: idField,
      nonIdFields: metadata.fields.filter((field) => !field.id),
      compIdFields: this.get_composite_id_fields(idField, metadataConfig),
    };
  }

  private static extract_entity_name(entityClassNm: string): string {
    return entityClassNm.replace("Entity", "");
  }

  private static set_field_pascal_name(fields: Field[]) {
    fields.forEach((field) => {
      field.fieldNmPascal = this.camel_to_pascal(field.fieldName);
    });
  }

  private static pascal_to_camel(pascal: string): string {
    return pascal.charAt(0).toLowerCase() + pascal.slice(1);
  }

  private static pascal_to_kebab(pascal: string): string {
    return pascal
      .replace(/([a-z])([A-Z])/g, "$1-$2")
      .replace(/[A-Z]/g, (letter) => letter.toLowerCase());
  }

  private static get_composite_id_fields(
    idField: Field,
    metadataConfig: MetadataConfig
  ): Field[] | undefined {
    const compIdFields = metadataConfig.list.find(
      (meta) => meta.className === idField.javaType
    )?.fields;

    if (compIdFields) {
      this.set_field_pascal_name(compIdFields);
    }

    return compIdFields;
  }

  private static camel_to_pascal(camel: string): string {
    return camel.charAt(0).toUpperCase() + camel.slice(1);
  }

  private static build_generate_targets_of_backend(
    templateData: TemplateData,
    destPaths: DestPaths,
    templateType: string
  ): GenerateTarget[] {
    const destBackDomainPkgPath = GeneratorUtils.build_dest_package_path(
      destPaths.destBackPath,
      templateData.domainPkgNm
    );

    const destBackIfPkgPath = GeneratorUtils.build_dest_package_path(
      destPaths.destBackPath,
      templateData.interfacesPkgNm
    );

    const generateTargets: GenerateTarget[] = [];

    if (templateType === "skeleton") {
      // Generate files for domain package
      generateTargets.push(
        {
          templatePath: `${templateType}/back/Repository.java`,
          destinationPath: `${destBackDomainPkgPath}/${templateData.entityNmPascal}Repository.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/Service.java`,
          destinationPath: `${destBackDomainPkgPath}/${templateData.entityNmPascal}Service.java`,
          templateData: templateData,
        }
      );

      // Generate files for interfaces package
      generateTargets.push(
        {
          templatePath: `${templateType}/back/Dto.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}Dto.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/Controller.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}Controller.java`,
          templateData: templateData,
        }
      );

      if (templateData.compIdFields) {
        generateTargets.push({
          templatePath: `${templateType}/back/IdDto.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}IdDto.java`,
          templateData: templateData,
        });
      }
    } else if (templateType === "arch") {
      // Generate files for domain package
      generateTargets.push(
        {
          templatePath: `${templateType}/back/Repository.java`,
          destinationPath: `${destBackDomainPkgPath}/${templateData.entityNmPascal}Repository.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/Service.java`,
          destinationPath: `${destBackDomainPkgPath}/${templateData.entityNmPascal}Service.java`,
          templateData: templateData,
        }
      );

      // Generate files for interfaces package
      generateTargets.push(
        {
          templatePath: `${templateType}/back/Dto.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}Dto.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/Controller.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}Controller.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/Factory.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}Factory.java`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/back/SearchCriteriaDto.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}SearchCriteriaDto.java`,
          templateData: templateData,
        }
      );

      if (templateData.compIdFields) {
        generateTargets.push({
          templatePath: `${templateType}/back/IdDto.java`,
          destinationPath: `${destBackIfPkgPath}/${templateData.entityNmPascal}IdDto.java`,
          templateData: templateData,
        });
      }
    }

    return generateTargets;
  }

  private static build_generate_targets_of_integrationtest(
    templateData: TemplateData,
    destPaths: DestPaths,
    templateType: string
  ): GenerateTarget[] {
    const destITPkgPath = GeneratorUtils.build_dest_package_path(
      destPaths.destITPath,
      templateData.interfacesPkgNm
    );

    return [
      {
        templatePath: `${templateType}/back/Client.java`,
        destinationPath: `${destITPkgPath}/${templateData.entityNmPascal}Client.java`,
        templateData: templateData,
      },
      {
        templatePath: `${templateType}/back/ControllerIT.java`,
        destinationPath: `${destITPkgPath}/${templateData.entityNmPascal}ControllerIT.java`,
        templateData: templateData,
      },
      {
        templatePath: `${templateType}/back/DataFactory.java`,
        destinationPath: `${destITPkgPath}/${templateData.entityNmPascal}DataFactory.java`,
        templateData: templateData,
      },
    ];
  }

  private static build_dest_package_path(
    destRootPath: string,
    pkgNm: string
  ): string {
    return `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }

  private static build_generate_targets_of_frontend(
    templateData: TemplateData,
    destPaths: DestPaths,
    templateType: string
  ): GenerateTarget[] {
    const entityPathCamel = `${destPaths.destFrontPath}/routes/${templateData.entityNmCamel}`;
    const entityPathPlural = `${destPaths.destFrontPath}/routes/${templateData.entityNmPlural}`;
    const forntendPagePath =
      GeneratorUtils.build_frontend_page_path(templateData);

    let generateTargets: GenerateTarget[] = [];

    if (templateType === "skeleton") {
      generateTargets = [
        {
          templatePath: `${templateType}/front/+page.svelte`,
          destinationPath: `${entityPathCamel}/+page.svelte`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/front/+page.ts`,
          destinationPath: `${entityPathCamel}/+page.ts`,
          templateData: templateData,
        },
      ];
    } else if (templateType === "arch") {
      generateTargets = [
        // For list page
        {
          templatePath: `${templateType}/front/routes/list/+page.svelte`,
          destinationPath: `${entityPathPlural}/+page.svelte`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/front/routes/list/+page.ts`,
          destinationPath: `${entityPathPlural}/+page.ts`,
          templateData: templateData,
        },

        // For create page
        {
          templatePath: `${templateType}/front/routes/new/+page.svelte`,
          destinationPath: `${entityPathPlural}/new/+page.svelte`,
          templateData,
        },
        {
          templatePath: `${templateType}/front/routes/new/+page.ts`,
          destinationPath: `${entityPathPlural}/new/+page.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/front/lib/Form.svelte`,
          destinationPath: `${destPaths.destFrontPath}/lib/domain/${templateData.entityNmPlural}/${templateData.entityNmPascal}Form.svelte`,
          templateData: templateData,
        },

        // For update page
        {
          templatePath: `${templateType}/front/routes/entityId/+page.svelte`,
          destinationPath: `${entityPathPlural}/${forntendPagePath}/+page.svelte`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/front/routes/entityId/+page.ts`,
          destinationPath: `${entityPathPlural}/${forntendPagePath}/+page.ts`,
          templateData: templateData,
        },
      ];
    }

    return generateTargets;
  }

  private static build_frontend_page_path(tmplData: TemplateData): string {
    const idFields = tmplData.compIdFields || [tmplData.idField];
    return idFields.map((idField) => `[${idField.fieldName}]`).join("/");
  }

  private static build_generate_targets_of_e2etest(
    templateData: TemplateData,
    destPaths: DestPaths,
    templateType: string
  ): GenerateTarget[] {
    const inputTmplPath = "e2etest/pages/input";
    const listTmplPath = "e2etest/pages/list";
    const inputDestPath = `${destPaths.destE2EPath}/pages/${templateData.entityNmKebab}-input`;
    const listDestPath = `${destPaths.destE2EPath}/pages/${templateData.entityNmKebab}-list`;

    let generateTargets: GenerateTarget[] = [];

    if (templateType === "skeleton") {
      generateTargets = [
        {
          templatePath: `${templateType}/e2etest/spec.ts`,
          destinationPath: `${destPaths.destE2EPath}/${GeneratorUtils.build_e2e_spec_path(templateData)}`,
          templateData: templateData,
        },
      ];
    } else if (templateType === "arch") {
      generateTargets = [
        {
          templatePath: `${templateType}/e2etest/spec.ts`,
          destinationPath: `${destPaths.destE2EPath}/${GeneratorUtils.build_e2e_spec_path(templateData)}`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/e2etest/Facade.ts`,
          destinationPath: `${destPaths.destE2EPath}/facades/${templateData.entityNmPascal}Facade.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/e2etest/Factory.ts`,
          destinationPath: `${destPaths.destE2EPath}/factories/${templateData.entityNmPascal}Factory.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/${inputTmplPath}/InputPage.ts`,
          destinationPath: `${inputDestPath}/${templateData.entityNmPascal}InputPage.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/${inputTmplPath}/InputPageElement.ts`,
          destinationPath: `${inputDestPath}/${templateData.entityNmPascal}InputPageElement.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/${listTmplPath}/ListPage.ts`,
          destinationPath: `${listDestPath}/${templateData.entityNmPascal}ListPage.ts`,
          templateData: templateData,
        },
        {
          templatePath: `${templateType}/${listTmplPath}/ListPageElement.ts`,
          destinationPath: `${listDestPath}/${templateData.entityNmPascal}ListPageElement.ts`,
          templateData: templateData,
        },
      ];
    }

    return generateTargets;
  }

  private static build_e2e_spec_path(tmplData: TemplateData): string {
    return `specs/${tmplData.domainPkgNm.split(".").slice(-1)[0]}/${tmplData.entityNmCamel}.spec.ts`;
  }
}
