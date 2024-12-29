import Generator from "yeoman-generator";
import { Metadata, TemplateData } from "./types.js";

const YO_RC_KEY_METADATA_FPATH = "metadataFilePath";
const YO_RC_KEY_DEST_PROJECT_PATH = "destProjectPath";
const YO_RC_KEY_DEST_MAIN_PATH = "destMainPath";
const YO_RC_KEY_DEST_IT_PATH = "destITPath";
const YO_RC_KEY_TEMPLATE_TYPE = "templateType";

class SvqkCodeGenerator extends Generator {
  metadataFilePath: string;
  destProjectPath: string;
  destMainPath: string;
  destITPath: string;
  templateType: string;
  metadataList: Metadata[];

  constructor(args: string | string[], opts: Record<string, unknown>) {
    super(args, opts);
    this.metadataFilePath = this.config.get(YO_RC_KEY_METADATA_FPATH);
    this.destProjectPath = this.config.get(YO_RC_KEY_DEST_PROJECT_PATH);
    this.destMainPath = this.config.get(YO_RC_KEY_DEST_MAIN_PATH);
    this.destITPath = this.config.get(YO_RC_KEY_DEST_IT_PATH);
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

      this._generate_backend(metaData);
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

  _output_java_file(
    component: string,
    destPkgPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(`${this.templateType}/java/${component}.java`),
      this.destinationPath(
        `${destPkgPath}/${tmplData.entityNmPascal}${component}.java`
      ),
      tmplData
    );
  }

  _extract_entity_name(entityClassNm: string): string {
    return entityClassNm.replace("Entity", "");
  }

  _generate_dest_package_path(destRootPath: string, pkgNm: string): string {
    return `${this.destProjectPath}/${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }

  _generate_backend(metadata: Metadata) {
    const tmplData = this._generate_template_data(metadata);

    // Generate files for domain package
    ["Repository", "Service"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destMainPath,
        tmplData.domainPkgNm
      );
      this._output_java_file(component, destPkgPath, tmplData);
    });

    // Generate files for interfaces package
    ["Dto", "Controller"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destMainPath,
        tmplData.interfacesPkgNm
      );
      this._output_java_file(component, destPkgPath, tmplData);
    });

    // Generate files for integration test
    ["Client", "ControllerIT"].forEach((component) => {
      const destPkgPath = this._generate_dest_package_path(
        this.destITPath,
        tmplData.interfacesPkgNm
      );
      this._output_java_file(component, destPkgPath, tmplData);
    });
  }
}

export default SvqkCodeGenerator;
