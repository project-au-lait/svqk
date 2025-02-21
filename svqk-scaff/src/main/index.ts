import { spawnSync } from "child_process";
import cpx from "cpx";
import fs from "fs";
import path from "node:path";
import pluralize from "pluralize";
import { generateApi } from "swagger-typescript-api";
import Generator from "yeoman-generator";
import {
  CustomOptions,
  Field,
  GenApiClientConfig,
  Metadata,
  MetadataConfig,
  OptionsValues,
  TemplateData,
} from "./types.js";

const allowedComponentValues = [
  "backend",
  "integration-test",
  "api-client",
  "frontend",
  "e2e-test",
  "all",
];
const allowedTemplateTypeValues = ["arch", "skeleton"];
const YO_RC_KEY_METADATA_FPATH = "metadataFilePath";
const YO_RC_KEY_DEST_BACK_PATH = "destBackPath";
const YO_RC_KEY_DEST_IT_PATH = "destITPath";
const YO_RC_KEY_DEST_FRONT_PATH = "destFrontPath";
const YO_RC_KEY_DEST_E2E_PATH = "destE2EPath";
const YO_RC_KEY_TEMPLATE_TYPE = "templateType";
const YO_RC_KEY_GEN_OPEN_API_JSON_CMD = "genOpenApiJsonCmd";
const YO_RC_KEY_GEN_ENTITY_CMD = "genEntityCmd";
const YO_RC_KEY_FRONT_API_CLIENT_PATH = "frontApiClientPath";
const YO_RC_KEY_E2E_API_CLIENT_PATH = "E2EApiClientPath";

class SvqkCodeGenerator extends Generator<CustomOptions> {
  optionsValues: OptionsValues;
  metadataConfig: MetadataConfig;
  genEntityCmd: string;
  destBackPath: string;
  destITPath: string;
  destFrontPath: string;
  destE2EPath: string;
  genApiClientConfig: GenApiClientConfig;

  constructor(args: string | string[], opts: CustomOptions) {
    super(args, opts);

    this.option("component", {
      type: String,
      default: "all",
    });

    if (
      this.options.component &&
      !allowedComponentValues.includes(this.options.component)
    ) {
      throw new Error(
        `Invalid value for option "--component": ${this.options.component}. Allowed values are: ${allowedComponentValues.join(
          ", "
        )}.`
      );
    }

    this.option("templateType", {
      type: String,
    });

    if (
      this.options.templateType &&
      !allowedTemplateTypeValues.includes(this.options.templateType)
    ) {
      throw new Error(
        `Invalid value for option "--templateType": ${this.options.templateType}. Allowed values are: ${allowedTemplateTypeValues.join(
          ", "
        )}.`
      );
    }

    this.optionsValues = {
      component: this.options.component,
      templateType:
        this.options.templateType || this.config.get(YO_RC_KEY_TEMPLATE_TYPE),
    };
    this.metadataConfig = {
      filePath: this.config.get(YO_RC_KEY_METADATA_FPATH),
      list: [],
    };
    this.genEntityCmd = this.config.get(YO_RC_KEY_GEN_ENTITY_CMD);
    this.destBackPath = this.config.get(YO_RC_KEY_DEST_BACK_PATH);
    this.destITPath = this.config.get(YO_RC_KEY_DEST_IT_PATH);
    this.destFrontPath = this.config.get(YO_RC_KEY_DEST_FRONT_PATH);
    this.destE2EPath = this.config.get(YO_RC_KEY_DEST_E2E_PATH);
    this.genApiClientConfig = {
      genOpenApiJsonCmd: this.config.get(YO_RC_KEY_GEN_OPEN_API_JSON_CMD),
      frontApiClientPath: this.config.get(YO_RC_KEY_FRONT_API_CLIENT_PATH),
      e2eApiClientPath: this.config.get(YO_RC_KEY_E2E_API_CLIENT_PATH),
    };
  }

  async initializing() {
    try {
      EntityGenerator.exec(this.genEntityCmd);

      if (this.optionsValues.component !== "api-client") {
        this.metadataConfig.list = await import(
          `${this.destinationRoot()}/${this.metadataConfig.filePath}`,
          { with: { type: "json" } }
        ).then((module) => module.default);

        if (
          !this.metadataConfig.list ||
          this.metadataConfig.list.length === 0
        ) {
          throw new Error(
            `The meta data list on ${this.metadataConfig.filePath} is empty.`
          );
        }
      }
    } catch (error) {
      this.log(`Failed to read ${this.metadataConfig.filePath}. ${error}`);
    }
  }

  writing() {
    const splitedArgs = this.args.flatMap((arg) => arg.trim().split(/\s+/));

    if (
      this.optionsValues.component !== "api-client" &&
      splitedArgs.length == 0
    ) {
      const entities = this.metadataConfig.list
        .map((metadata) => metadata.className)
        .join(", ");
      this.log(
        `Please specify entity name(s) with space separated choosing from ${entities}.`
      );
      return;
    }

    this.metadataConfig.list.forEach((metaData) => {
      if (
        !splitedArgs.includes(metaData.className) &&
        !splitedArgs.includes(this._extract_entity_name(metaData.className))
      ) {
        return;
      }

      const tmplData = this._generate_template_data(metaData);

      switch (this.optionsValues.component) {
        case "backend":
          this._generate_backend(tmplData);
          break;
        case "integration-test":
          this._generate_integrationtest(tmplData);
          break;
        case "frontend":
          this._generate_frontend(tmplData);
          break;
        case "e2e-test":
          this._generate_e2etest(tmplData);
          break;
        case "all":
          this._generate_backend(tmplData);
          this._generate_integrationtest(tmplData);
          this._generate_frontend(tmplData);
          this._generate_e2etest(tmplData);
          break;
      }
    });
  }

  end() {
    if (
      this.optionsValues.component === "api-client" ||
      this.optionsValues.component === "all"
    ) {
      ApiClientGenerator.exec(this.genApiClientConfig);
    }

    this.log("Completed.");
  }

  _generate_template_data(metadata: Metadata): TemplateData {
    const entityNmPascal = this._extract_entity_name(metadata.className);
    const idField = metadata.fields.find((field) => field.id) ?? ({} as Field);

    this._set_field_pascal_name(metadata.fields);

    return {
      domainPkgNm: metadata.packageName,
      interfacesPkgNm: metadata.packageName.replace(".domain.", ".interfaces."),
      entityNmPascal: entityNmPascal,
      entityNmCamel: this._pascal_to_camel(entityNmPascal),
      entityNmAllCaps: entityNmPascal.toUpperCase(),
      entityNmPlural: pluralize(entityNmPascal.toLowerCase()),
      fields: metadata.fields,
      idField: idField,
      compIdFields: this._get_composite_id_fields(idField),
    };
  }

  _get_composite_id_fields(idField: Field): Field[] | undefined {
    const compIdFields = this.metadataConfig.list.find(
      (meta) => meta.className === idField.javaType
    )?.fields;

    if (compIdFields) {
      this._set_field_pascal_name(compIdFields);
    }

    return compIdFields;
  }

  _set_field_pascal_name(fields: Field[]) {
    fields.forEach((field) => {
      field.fieldNmPascal = this._camel_to_pascal(field.fieldName);
    });
  }

  _output_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(`${this.optionsValues.templateType}/${tmplPath}`),
      this.destinationPath(destinationPath),
      tmplData
    );
  }

  _output_back_file(
    components: string[],
    destPkgPath: string,
    tmplData: TemplateData
  ) {
    components.forEach((component) => {
      this._output_file(
        `back/${component}.java`,
        `${destPkgPath}/${tmplData.entityNmPascal}${component}.java`,
        tmplData
      );
    });
  }

  _output_front_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this._output_file(tmplPath, destinationPath, tmplData);
  }

  _output_e2etest_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this._output_file(tmplPath, destinationPath, tmplData);
  }

  _pascal_to_camel(pascal: string): string {
    return pascal.charAt(0).toLowerCase() + pascal.slice(1);
  }

  _camel_to_pascal(camel: string): string {
    return camel.charAt(0).toUpperCase() + camel.slice(1);
  }

  _extract_entity_name(entityClassNm: string): string {
    return entityClassNm.replace("Entity", "");
  }

  _generate_dest_package_path(destRootPath: string, pkgNm: string): string {
    return `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }

  _generate_e2e_spec_path(tmplData: TemplateData): string {
    return `specs/${tmplData.domainPkgNm.split(".").slice(-1)[0]}/${tmplData.entityNmCamel}.spec.ts`;
  }

  _generate_backend(tmplData: TemplateData) {
    const destBackDomainPkgPath = this._generate_dest_package_path(
      this.destBackPath,
      tmplData.domainPkgNm
    );

    const destBackIfPkgPath = this._generate_dest_package_path(
      this.destBackPath,
      tmplData.interfacesPkgNm
    );

    if (
      this.optionsValues.templateType === "arch" ||
      this.optionsValues.templateType === "skeleton"
    ) {
      // Generate files for domain package
      this._output_back_file(
        ["Repository", "Service"],
        destBackDomainPkgPath,
        tmplData
      );

      // Generate files for interfaces package
      this._output_back_file(
        ["Dto", "Controller"],
        destBackIfPkgPath,
        tmplData
      );

      if (tmplData.compIdFields) {
        this._output_back_file(["IdDto"], destBackIfPkgPath, tmplData);
      }
    }

    if (this.optionsValues.templateType === "arch") {
      // Generate files for interfaces package
      this._output_back_file(
        ["Factory", "SearchCriteriaDto"],
        destBackIfPkgPath,
        tmplData
      );
    }
  }

  _generate_integrationtest(tmplData: TemplateData) {
    const destITPkgPath = this._generate_dest_package_path(
      this.destITPath,
      tmplData.interfacesPkgNm
    );
    this._output_back_file(
      ["Client", "ControllerIT", "DataFactory"],
      destITPkgPath,
      tmplData
    );
  }

  _generate_frontend(tmplData: TemplateData) {
    let pathPairs: [string, string][] = [];

    const entityPathCamel = `${this.destFrontPath}/routes/${tmplData.entityNmCamel}`;
    const entityPathPlural = `${this.destFrontPath}/routes/${tmplData.entityNmPlural}`;

    if (this.optionsValues.templateType === "skeleton") {
      pathPairs = [
        ["front/+page.svelte", `${entityPathCamel}/+page.svelte`],
        ["front/+page.ts", `${entityPathCamel}/+page.ts`],
      ];
    } else if (this.optionsValues.templateType === "arch") {
      pathPairs = [
        // For list screen
        ["front/routes/list/+page.svelte", `${entityPathPlural}/+page.svelte`],
        ["front/routes/list/+page.ts", `${entityPathPlural}/+page.ts`],

        // For create screen
        [
          "front/routes/new/+page.svelte",
          `${entityPathPlural}/new/+page.svelte`,
        ],
        ["front/routes/new/+page.ts", `${entityPathPlural}/new/+page.ts`],
        [
          "front/lib/Form.svelte",
          `${this.destFrontPath}/lib/domain/${tmplData.entityNmPlural}/${tmplData.entityNmPascal}Form.svelte`,
        ],

        // For update screen
        [
          "front/routes/entityId/+page.svelte",
          `${entityPathPlural}/[entityId]/+page.svelte`,
        ],
        [
          "front/routes/entityId/+page.ts",
          `${entityPathPlural}/[entityId]/+page.ts`,
        ],
      ];
    }

    for (const [srcPath, destPath] of pathPairs) {
      this._output_front_file(srcPath, destPath, tmplData);
    }
  }

  _generate_e2etest(tmplData: TemplateData) {
    let pathPairs: [string, string][] = [];

    const inputTmplPath = "e2etest/pages/input";
    const listTmplPath = "e2etest/pages/list";
    const menuBarTmplPath = "e2etest/pages/menu-bar";
    const inputDestPath = `${this.destE2EPath}/pages/${tmplData.entityNmCamel}-input`;
    const listDestPath = `${this.destE2EPath}/pages/${tmplData.entityNmCamel}-list`;
    const menuBarDestPath = `${this.destE2EPath}/pages/menu-bar`;

    // TODO temporary
    if (this.optionsValues.templateType === "skeleton") {
      pathPairs = [
        [
          "e2etest/spec.ts",
          `${this.destE2EPath}/${this._generate_e2e_spec_path(tmplData)}`,
        ],
      ];
    } else if (this.optionsValues.templateType === "arch") {
      pathPairs = [
        [
          "e2etest/spec.ts",
          `${this.destE2EPath}/${this._generate_e2e_spec_path(tmplData)}`,
        ],
        [
          "e2etest/Facade.ts",
          `${this.destE2EPath}/facades/${tmplData.entityNmPascal}Facade.ts`,
        ],
        [
          "e2etest/Factory.ts",
          `${this.destE2EPath}/factories/${tmplData.entityNmPascal}Factory.ts`,
        ],
        [`${menuBarTmplPath}/MenuBar.ts`, `${menuBarDestPath}/MenuBar.ts`],
        [
          `${menuBarTmplPath}/MenuBarPageElement.ts`,
          `${menuBarDestPath}/MenuBarPageElement.ts`,
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

      // Add link to EntityListPage in layout
      this._output_front_file(
        "e2etest/+layout.svelte",
        `${this.destFrontPath}/routes/+layout.svelte`,
        tmplData
      );
    }

    for (const [srcPath, destPath] of pathPairs) {
      this._output_e2etest_file(srcPath, destPath, tmplData);
    }
  }
}

class ExternalCommandExecutor {
  static exec(cmd: string, env?: NodeJS.ProcessEnv) {
    const isWin = process.platform === "win32";
    const shell = isWin ? { cmd: "cmd", arg: "/C" } : { cmd: "sh", arg: "-c" };

    env = { ...process.env, ...env };

    if (isWin) {
      cmd = cmd.replace("./mvnw", "mvnw");
    }

    console.log(`exec: ${cmd}`);

    spawnSync(shell.cmd, [shell.arg, cmd], {
      cwd: "../",
      env: env,
      stdio: "inherit",
    });
  }
}

class EntityGenerator {
  static exec(genEntitycmd: string) {
    const env = {
      MAVEN_OPTS:
        "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    };

    ExternalCommandExecutor.exec(genEntitycmd, env);
  }
}

class ApiClientGenerator {
  static exec(genApiClientConfig: GenApiClientConfig) {
    ExternalCommandExecutor.exec(genApiClientConfig.genOpenApiJsonCmd);
    this._exec_gen_api_client(
      genApiClientConfig.frontApiClientPath,
      genApiClientConfig.e2eApiClientPath
    );
  }

  private static _exec_gen_api_client(
    frontApiClientPath: string,
    e2eApiClientPath: string
  ) {
    const openApiJsonPath = path.resolve(
      process.cwd(),
      "./target/openapi.json"
    );
    console.log("openapi.json Path:", openApiJsonPath);
    if (!fs.existsSync(openApiJsonPath)) {
      throw new Error("openapi.json is not found.");
    }

    generateApi({
      name: "Api.ts",
      input: openApiJsonPath,
      output: path.resolve(process.cwd(), frontApiClientPath),
      moduleNameIndex: 1,
      hooks: {
        onFormatRouteName: (routeInfo, templateRouteName) => {
          const newTemplateRouteName = templateRouteName.replace(
            /SearchCreate$/,
            "Search"
          );

          console.log(
            `Replace templateRouteName ${templateRouteName} to ${newTemplateRouteName}`
          );
          return newTemplateRouteName;
        },
        onFormatTypeName: (typeName, rawTypeName, schemaType) => {
          if (schemaType === "type-name" && typeName.endsWith("Dto")) {
            const newTypeName = typeName.replace(/Dto$/, "Model");
            console.log(`Replace typeName ${typeName} to ${newTypeName}`);
            return newTypeName;
          }

          return typeName;
        },
      },
    })
      .then(() => {
        this._exec_copy_api(frontApiClientPath, e2eApiClientPath);
        console.log("Client API generated successfully!");
      })
      .catch((err) => {
        throw new Error(`Error generating Client API: ${err}`);
      });
  }

  private static _exec_copy_api(
    frontApiClientPath: string,
    e2eApiClientPath: string
  ) {
    cpx.copy(`${frontApiClientPath}/Api.ts`, e2eApiClientPath, (err) => {
      if (err) {
        throw new Error(`Copy failed: ${err}`);
      } else {
        console.log("Files copied successfully.");
      }
    });
  }
}

export default SvqkCodeGenerator;
