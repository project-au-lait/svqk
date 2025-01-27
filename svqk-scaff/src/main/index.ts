import Generator from "yeoman-generator";
import { Metadata, TemplateData } from "./types.js";

const YO_RC_KEY_METADATA_FPATH = "metadataFilePath";
const YO_RC_KEY_DEST_BACK_PATH = "destBackPath";
const YO_RC_KEY_DEST_IT_PATH = "destITPath";
const YO_RC_KEY_DEST_FRONT_PATH = "destFrontPath";
const YO_RC_KEY_DEST_E2E_PATH = "destE2EPath";
const YO_RC_KEY_TEMPLATE_TYPE = "templateType";

class SvqkCodeGenerator extends Generator {
  metadataFilePath: string;
  destBackPath: string;
  destITPath: string;
  destFrontPath: string;
  destE2EPath: string;
  templateType: string;
  metadataList: Metadata[];

  constructor(args: string | string[], opts: Record<string, unknown>) {
    super(args, opts);
    this.metadataFilePath = this.config.get(YO_RC_KEY_METADATA_FPATH);
    this.destBackPath = this.config.get(YO_RC_KEY_DEST_BACK_PATH);
    this.destITPath = this.config.get(YO_RC_KEY_DEST_IT_PATH);
    this.destFrontPath = this.config.get(YO_RC_KEY_DEST_FRONT_PATH);
    this.destE2EPath = this.config.get(YO_RC_KEY_DEST_E2E_PATH);
    this.templateType = this.config.get(YO_RC_KEY_TEMPLATE_TYPE);
    this.metadataList = [];
  }

  async initializing() {
    try {
      this.metadataList = await import(
        `${this.destinationRoot()}/${this.metadataFilePath}`,
        { with: { type: "json" } }
      ).then((module) => module.default);

      if (!this.metadataList || this.metadataList.length === 0) {
        throw new Error(
          `The meta data list on ${this.metadataFilePath} is empty.`
        );
      }
    } catch (error) {
      this.log(`Failed to read ${this.metadataFilePath}. ${error}`);
    }
  }

  writing() {
    if (this.args.length == 0) {
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
        !this.args.includes(metaData.className) &&
        !this.args.includes(this._extract_entity_name(metaData.className))
      ) {
        return;
      }

      const tmplData = this._generate_template_data(metaData);

      this._generate_backend(tmplData);
      this._generate_frontend(tmplData);
      this._generate_e2etest(tmplData);
    });
  }

  end() {
    this.log("Completed.");
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
      fields: metadata.fields,
    };
  }

  _output_file(
    templatePath: string,
    destinationPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(`${this.templateType}/${templatePath}`),
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

  _output_front_file(component: string, tmplData: TemplateData) {
    this._output_file(
      `front/${component}`,
      `${this.destFrontPath}/${tmplData.entityNmCamel}/${component}`,
      tmplData
    );
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
    // Generate files for domain package
    ["Repository", "Service"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destBackPath,
        tmplData.domainPkgNm
      );
      this._output_back_file(component, destPkgPath, tmplData);
    });

    const destBackIfPkgPath = this._generate_dest_package_path(
      this.destBackPath,
      tmplData.interfacesPkgNm
    );

    // Generate files for interfaces package
    ["Dto", "Controller"].forEach((component) => {
      this._output_back_file(component, destBackIfPkgPath, tmplData);
    });

    // TODO refactor with later additional implementation
    if (this.templateType === "arch") {
      ["Factory", "SearchCriteriaDto"].forEach((component) => {
        this._output_back_file(component, destBackIfPkgPath, tmplData);
      });
    }

    // Generate files for integration test
    ["Client", "ControllerIT", "DataFactory"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destITPath,
        tmplData.interfacesPkgNm
      );
      this._output_back_file(component, destPkgPath, tmplData);
    });
  }

  _generate_frontend(tmplData: TemplateData) {
    ["+page.svelte", "+page.ts"].forEach((component) => {
      this._output_front_file(component, tmplData);
    });
  }

  _generate_e2etest(tmplData: TemplateData) {
    this._output_e2etest_file(
      "spec",
      this._generate_e2e_spec_path(tmplData),
      tmplData
    );
  }
}

export default SvqkCodeGenerator;
