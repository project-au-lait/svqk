import { spawnSync } from "child_process";
import Generator from "yeoman-generator";
import type { GeneratorOptions } from "@yeoman/types";
import { generateApi } from "swagger-typescript-api";
import path from "node:path";
import fs from "fs";
import cpx from "cpx";
import { Metadata, TemplateData } from "./types.js";
import pluralize from "pluralize";

type CustomOptions = GeneratorOptions & {
  component: string;
  templateType: string;
};

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
  metadataFilePath: string;
  destBackPath: string;
  destITPath: string;
  destFrontPath: string;
  destE2EPath: string;
  component: string;
  templateType: string;
  genOpenApiJsonCmd: string;
  genEntityCmd: string;
  frontApiClientPath: string;
  e2eApiClientPath: string;
  metadataList: Metadata[];

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

    this.metadataFilePath = this.config.get(YO_RC_KEY_METADATA_FPATH);
    this.destBackPath = this.config.get(YO_RC_KEY_DEST_BACK_PATH);
    this.destITPath = this.config.get(YO_RC_KEY_DEST_IT_PATH);
    this.destFrontPath = this.config.get(YO_RC_KEY_DEST_FRONT_PATH);
    this.destE2EPath = this.config.get(YO_RC_KEY_DEST_E2E_PATH);
    this.component = this.options.component;
    this.templateType =
      this.options.templateType || this.config.get(YO_RC_KEY_TEMPLATE_TYPE);
    this.genOpenApiJsonCmd = this.config.get(YO_RC_KEY_GEN_OPEN_API_JSON_CMD);
    this.genEntityCmd = this.config.get(YO_RC_KEY_GEN_ENTITY_CMD);
    this.frontApiClientPath = this.config.get(YO_RC_KEY_FRONT_API_CLIENT_PATH);
    this.e2eApiClientPath = this.config.get(YO_RC_KEY_E2E_API_CLIENT_PATH);
    this.metadataList = [];
  }

  async initializing() {
    try {
      // TODO Add an option not to be executed when cicd.
      this._exec_gen_entity();

      if (this.component !== "api-client") {
        this.metadataList = await import(
          `${this.destinationRoot()}/${this.metadataFilePath}`,
          { with: { type: "json" } }
        ).then((module) => module.default);

        if (!this.metadataList || this.metadataList.length === 0) {
          throw new Error(
            `The meta data list on ${this.metadataFilePath} is empty.`
          );
        }
      }
    } catch (error) {
      this.log(`Failed to read ${this.metadataFilePath}. ${error}`);
    }
  }

  writing() {
    const splitedArgs = this.args.flatMap((arg) => arg.trim().split(/\s+/));

    if (this.component !== "api-client" && splitedArgs.length == 0) {
      const entities = this.metadataList
        .map((metadata) => metadata.className)
        .join(", ");
      this.log(
        `Please specify entity name(s) with space separated choosing from ${entities}.`
      );
      return;
    }

    this.metadataList.forEach((metaData) => {
      if (
        !splitedArgs.includes(metaData.className) &&
        !splitedArgs.includes(this._extract_entity_name(metaData.className))
      ) {
        return;
      }

      const tmplData = this._generate_template_data(metaData);

      switch (this.component) {
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

    if (this.component === "api-client" || this.component === "all") {
      this._generate_apiclient();
    }
  }

  end() {
    this.log("Completed.");
  }

  _exec_cmd(cmd: string, env?: NodeJS.ProcessEnv) {
    const isWin = process.platform === "win32";
    const shell = isWin ? { cmd: "cmd", arg: "/C" } : { cmd: "sh", arg: "-c" };

    env = { ...process.env, ...env };

    if (isWin) {
      cmd = cmd.replace("./mvnw", "mvnw");
    }

    this.log(`exec: ${this.genEntityCmd}`);

    spawnSync(shell.cmd, [shell.arg, cmd], {
      cwd: "../",
      env: env,
      stdio: "inherit",
    });
  }

  _exec_gen_open_api_json() {
    this._exec_cmd(this.genOpenApiJsonCmd);
  }

  _exec_gen_api_client() {
    const openApiJsonPath = path.resolve(
      process.cwd(),
      "./target/openapi.json"
    );
    this.log("openapi.json Path:", openApiJsonPath);
    if (!fs.existsSync(openApiJsonPath)) {
      throw new Error("openapi.json is not found.");
    }

    generateApi({
      name: "Api.ts",
      input: openApiJsonPath,
      output: path.resolve(process.cwd(), this.frontApiClientPath),
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
        this._exec_copy_api();
        this.log("Client API generated successfully!");
      })
      .catch((err) => {
        throw new Error(`Error generating Client API: ${err}`);
      });
  }

  _exec_copy_api() {
    cpx.copy(
      `${this.frontApiClientPath}/Api.ts`,
      this.e2eApiClientPath,
      (err) => {
        if (err) {
          throw new Error(`Copy failed: ${err}`);
        } else {
          this.log("Files copied successfully.");
        }
      }
    );
  }

  _generate_apiclient() {
    this._exec_gen_open_api_json();
    this._exec_gen_api_client();
  }

  _exec_gen_entity() {
    const env = {
      MAVEN_OPTS:
        "--add-exports=jdk.compiler/com.sun.tools.javac.api=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.code=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.file=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.parser=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.tree=ALL-UNNAMED --add-exports=jdk.compiler/com.sun.tools.javac.util=ALL-UNNAMED",
    };

    this._exec_cmd(this.genEntityCmd, env);
  }

  _generate_template_data(metadata: Metadata): TemplateData {
    const entityNmPascal = this._extract_entity_name(metadata.className);

    return {
      domainPkgNm: metadata.packageName,
      interfacesPkgNm: metadata.packageName.replace(".domain.", ".interfaces."),
      entityNmPascal: entityNmPascal,
      entityNmCamel:
        entityNmPascal.charAt(0).toLowerCase() + entityNmPascal.slice(1),
      entityNmAllCaps: entityNmPascal.toUpperCase(),
      entityNmPlural: pluralize(entityNmPascal.toLowerCase()),
      fields: metadata.fields,
    };
  }

  _output_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(`${this.templateType}/${tmplPath}`),
      this.destinationPath(destinationPath),
      tmplData
    );
  }

  _output_back_file(
    component: string,
    destPkgPath: string,
    tmplData: TemplateData
  ) {
    this._output_file(
      `back/${component}.java`,
      `${destPkgPath}/${tmplData.entityNmPascal}${component}.java`,
      tmplData
    );
  }

  _output_front_file(
    tmplPath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this._output_file(tmplPath, destinationPath, tmplData);
  }

  _output_e2etest_file(
    component: string,
    destpath: string,
    tmplData: TemplateData
  ) {
    this._output_file(
      `e2etest/${component}.ts`,
      `${this.destE2EPath}/${destpath}`,
      tmplData
    );
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
    if (this.templateType === "arch" || this.templateType === "skeleton") {
      // Generate files for domain package
      ["Repository", "Service"].forEach((component) => {
        const destPkgPath = this._generate_dest_package_path(
          this.destBackPath,
          tmplData.domainPkgNm
        );
        this._output_back_file(component, destPkgPath, tmplData);
      });

      // Generate files for interfaces package
      ["Dto", "Controller"].forEach((component) => {
        const destBackIfPkgPath = this._generate_dest_package_path(
          this.destBackPath,
          tmplData.interfacesPkgNm
        );
        this._output_back_file(component, destBackIfPkgPath, tmplData);
      });
    }

    if (this.templateType === "arch") {
      // Generate files for interfaces package
      ["Factory", "SearchCriteriaDto"].forEach((component) => {
        const destBackIfPkgPath = this._generate_dest_package_path(
          this.destBackPath,
          tmplData.interfacesPkgNm
        );
        this._output_back_file(component, destBackIfPkgPath, tmplData);
      });
    }
  }

  _generate_integrationtest(tmplData: TemplateData) {
    ["Client", "ControllerIT", "DataFactory"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destITPath,
        tmplData.interfacesPkgNm
      );
      this._output_back_file(component, destPkgPath, tmplData);
    });
  }

  _generate_frontend(tmplData: TemplateData) {
    if (this.templateType === "skeleton") {
      ["+page.svelte", "+page.ts"].forEach((component) => {
        this._output_front_file(
          `front/${component}`,
          `${this.destFrontPath}/routes/${tmplData.entityNmCamel}/${component}`,
          tmplData
        );
      });
    } else if (this.templateType === "arch") {
      fs.mkdirSync(`${this.destFrontPath}/routes/${tmplData.entityNmPlural}`);

      ["+page.svelte", "+page.ts"].forEach((component) => {
        this._output_front_file(
          `front/routes/list/${component}`,
          `${this.destFrontPath}/routes/${tmplData.entityNmPlural}/${component}`,
          tmplData
        );
      });
    }
  }

  _generate_e2etest(tmplData: TemplateData) {
    // TODO temporary
    if (this.templateType === "skeleton") {
      this._output_e2etest_file(
        "spec",
        this._generate_e2e_spec_path(tmplData),
        tmplData
      );
    }
  }
}

export default SvqkCodeGenerator;
