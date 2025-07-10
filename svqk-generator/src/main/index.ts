import Generator from "yeoman-generator";
import {
  CustomOptions,
  GenApiClientConfig,
  MetadataConfig,
  OptionsValues,
  TemplateData,
  DestPaths,
} from "./types.js";
import { GeneratorUtils } from "./lib/GeneratorUtils.js";
import { BackendGenerator } from "./lib/BackendGenerator.js";
import { FrontendGenerator } from "./lib/FrontendGenerator.js";
import { EntityGenerator } from "./lib/EntityGenerator.js";
import { ApiClientGenerator } from "./lib/ApiClientGenerator.js";
import { MenuEditor } from "./lib/MenuEditor.js";

const allowedComponentValues = [
  "entity",
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
  optionsValues: OptionsValues = {
    component: "",
    templateType: "",
  };
  inputTables: string[] = [];
  metadataConfig: MetadataConfig = {
    filePath: "",
    list: [],
  };
  genEntityCmd: string = "";
  destPaths: DestPaths = {
    destBackPath: "",
    destITPath: "",
    destFrontPath: "",
    destE2EPath: "",
  };
  genApiClientConfig: GenApiClientConfig = {
    genOpenApiJsonCmd: "",
    frontApiClientPath: "",
    e2eApiClientPath: "",
  };
  generateEntity: boolean | null = null;
  backendGenerator: BackendGenerator | null = null;
  frontendGenerator: FrontendGenerator | null = null;
  menuEditor: MenuEditor | null = null;
  templateDataList: TemplateData[] = [];
  metadataConfigPath: string = "";

  constructor(args: string | string[], opts: CustomOptions) {
    super(args, opts);

    this.option("component", {
      type: String,
    });

    this.option("templateType", {
      type: String,
    });
  }

  async initializing() {
    this.optionsValues = {
      component: this.options.component,
      templateType:
        this.options.templateType || this.config.get(YO_RC_KEY_TEMPLATE_TYPE),
    };

    this.inputTables = this.args
      .filter((arg) => typeof arg === "string" && arg.trim())
      .flatMap((arg) => arg.trim().split(/\s+/));

    this.metadataConfig = {
      filePath: this.config.get(YO_RC_KEY_METADATA_FPATH),
      list: [],
    };
    this.genEntityCmd = this.config.get(YO_RC_KEY_GEN_ENTITY_CMD);

    this.destPaths = {
      destBackPath: this.config.get(YO_RC_KEY_DEST_BACK_PATH),
      destITPath: this.config.get(YO_RC_KEY_DEST_IT_PATH),
      destFrontPath: this.config.get(YO_RC_KEY_DEST_FRONT_PATH),
      destE2EPath: this.config.get(YO_RC_KEY_DEST_E2E_PATH),
    };

    this.genApiClientConfig = {
      genOpenApiJsonCmd: this.config.get(YO_RC_KEY_GEN_OPEN_API_JSON_CMD),
      frontApiClientPath: this.config.get(YO_RC_KEY_FRONT_API_CLIENT_PATH),
      e2eApiClientPath: this.config.get(YO_RC_KEY_E2E_API_CLIENT_PATH),
    };

    this.metadataConfigPath = `${this.destinationRoot()}/${this.metadataConfig.filePath}`;

    this.metadataConfig.list = await GeneratorUtils.load_json_file(
      this.metadataConfigPath
    );

    this.backendGenerator = new BackendGenerator(
      this.fs,
      this.templatePath.bind(this),
      this.destinationPath.bind(this),
      this.optionsValues.templateType,
      this.destPaths
    );

    this.frontendGenerator = new FrontendGenerator(
      this.fs,
      this.templatePath.bind(this),
      this.destinationPath.bind(this),
      this.optionsValues.templateType,
      this.destPaths
    );

    this.menuEditor = new MenuEditor(
      this.fs,
      this.optionsValues.templateType,
      this.destPaths
    );
  }

  async prompting() {
    if (!this.optionsValues.component) {
      const answer = await this.prompt([
        {
          type: "list" as const,
          name: "component",
          message: "Select a component:",
          choices: allowedComponentValues,
        },
      ]);
      this.optionsValues.component = answer.component;
    }

    if (
      this.inputTables.length === 0 &&
      !["entity", "api-client"].includes(this.optionsValues.component)
    ) {
      const answer = await this.prompt([
        {
          type: "input" as const,
          name: "tables",
          message: "Enter tables (e.g. tablea_name tableb_name tablec_name):",
        },
      ]);

      this.inputTables = answer.tables ? answer.tables.trim().split(/\s+/) : [];
    }

    if (
      !this.metadataConfig.list &&
      this.optionsValues.component !== "all" &&
      this.optionsValues.component !== "entity"
    ) {
      const answer = await this.prompt([
        {
          type: "list" as const,
          name: "generateEntity",
          message: "Do you want to generate entities?",
          choices: ["Yes", "No"],
          filter: (input) => input === "Yes",
        },
      ]);

      this.generateEntity = answer.generateEntity;
    }
  }

  async configuring() {
    if (
      this.optionsValues.component &&
      !allowedComponentValues.includes(this.optionsValues.component)
    ) {
      throw new Error(
        `Invalid value for option "--component": ${this.optionsValues.component}. Allowed values are: ${allowedComponentValues.join(
          ", "
        )}.`
      );
    }

    if (
      this.optionsValues.templateType &&
      !allowedTemplateTypeValues.includes(this.optionsValues.templateType)
    ) {
      throw new Error(
        `Invalid value for option "--templateType": ${this.optionsValues.templateType}. Allowed values are: ${allowedTemplateTypeValues.join(
          ", "
        )}.`
      );
    }

    if (
      this.optionsValues.component === "entity" ||
      this.optionsValues.component === "all" ||
      this.generateEntity
    ) {
      EntityGenerator.exec(this.genEntityCmd);

      this.metadataConfig.list = await GeneratorUtils.load_json_file(
        this.metadataConfigPath
      );
    }

    if (
      this.optionsValues.component !== "api-client" &&
      this.optionsValues.component !== "entity" &&
      this.inputTables.length == 0
    ) {
      const tables = this.metadataConfig.list
        .map((metadata) => metadata.tableName)
        .filter((name) => typeof name === "string" && name.trim() !== "")
        .join(", ");

      throw new Error(
        `Please specify table name(s) with space separated choosing from ${tables}.`
      );
    }

    if (
      this.optionsValues.component !== "api-client" &&
      this.optionsValues.component !== "entity" &&
      !this.metadataConfig.list
    ) {
      throw new Error("Please generate entities.");
    }

    this.metadataConfig.list.forEach((metaData) => {
      if (
        this.inputTables.includes(metaData.tableName) ||
        this.inputTables.includes(metaData.className)
      ) {
        const tmplData = GeneratorUtils.build_template_data(
          metaData,
          this.metadataConfig
        );

        this.templateDataList.push(tmplData);
      }
    });
  }

  writing() {
    if (
      this.optionsValues.component === "api-client" ||
      this.optionsValues.component === "entity"
    ) {
      return;
    }

    this.templateDataList.forEach((templateData) => {
      switch (this.optionsValues.component) {
        case "backend":
          this.backendGenerator?.generate_backend(templateData);
          break;
        case "integration-test":
          this.backendGenerator?.generate_integrationtest(templateData);
          break;
        case "frontend":
          this.frontendGenerator?.generate_frontend(templateData);
          this.menuEditor?.update_menu(templateData);
          break;
        case "e2e-test":
          this.frontendGenerator?.generate_e2etest(templateData);
          this.menuEditor?.update_menu(templateData);
          this.menuEditor?.update_menu_test(templateData);
          break;
        case "all":
          this.backendGenerator?.generate_backend(templateData);
          this.backendGenerator?.generate_integrationtest(templateData);
          this.frontendGenerator?.generate_frontend(templateData);
          this.frontendGenerator?.generate_e2etest(templateData);
          this.menuEditor?.update_menu(templateData);
          this.menuEditor?.update_menu_test(templateData);
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
  }
}

export default SvqkCodeGenerator;
