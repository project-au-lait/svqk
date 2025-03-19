import Generator from "yeoman-generator";
import {
  CustomOptions,
  GenApiClientConfig,
  MetadataConfig,
  OptionsValues,
  TemplateData,
  GenerateTarget,
  DestPaths,
  SnippetInsertionTarget,
} from "./types.js";
import { GeneratorUtils } from "./lib/generator-utils.js";
import { EntityGenerator } from "./lib/entity-generator.js";
import { ApiClientGenerator } from "./lib/api-client-generator.js";

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

const LINE_BREAK = "\n";
const INDENT = " ";

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

    this.metadataConfig.list = await GeneratorUtils.load_json_file(
      `${this.destinationRoot()}/${this.metadataConfig.filePath}?t=${Date.now()}`
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
        `${this.destinationRoot()}/${this.metadataConfig.filePath}?t=${Date.now()}`
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
  }

  writing() {
    if (
      this.optionsValues.component === "api-client" ||
      this.optionsValues.component === "entity"
    ) {
      return;
    }

    const generateTargets: GenerateTarget[] =
      GeneratorUtils.build_generate_targets(
        this.metadataConfig,
        this.optionsValues.component,
        this.optionsValues.templateType,
        this.inputTables,
        this.destPaths
      );

    generateTargets.forEach((target) => {
      this._generate_file(
        target.templatePath,
        target.destinationPath,
        target.templateData
      );
    });

    if (
      this.optionsValues.component === "all" ||
      this.optionsValues.component === "e2etest"
    ) {
      const snippetInsertionTargets: SnippetInsertionTarget[] =
        GeneratorUtils.build_snippet_insert_targets(
          this.metadataConfig,
          this.optionsValues.component,
          this.optionsValues.templateType,
          this.inputTables,
          this.destPaths
        );
      snippetInsertionTargets.forEach((target) => {
        this._insert_snippet(
          target.templatePath,
          target.destinationPath,
          target.placeholder,
          target.templateData
        );
      });
    }
  }

  end() {
    if (
      this.optionsValues.component === "api-client" ||
      this.optionsValues.component === "all"
    ) {
      ApiClientGenerator.exec(this.genApiClientConfig);
    }
  }

  _generate_file(
    templatePath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(templatePath),
      this.destinationPath(destinationPath),
      tmplData
    );
  }

  _insert_snippet(
    templatePath: string,
    destinationPath: string,
    placeholder: string,
    templateData: TemplateData
  ) {
    const renderedPath = templatePath.replace(".ejs", ".txt");
    this.fs.copyTpl(templatePath, renderedPath, templateData);

    const renderedText = this.fs.read(renderedPath);

    if (renderedText) {
      this.fs.copy(destinationPath, destinationPath, {
        process: function (content) {
          const originalText = content.toString();
          const regex = new RegExp(`^.*__${placeholder}__.*${LINE_BREAK}`, "m");
          const placeholderLine = RegExp(regex).exec(originalText);
          return originalText.replace(
            regex,
            renderedText + LINE_BREAK + placeholderLine
          );
        },
      });

      this.fs.delete(renderedPath);
    }
  }
}

export default SvqkCodeGenerator;
