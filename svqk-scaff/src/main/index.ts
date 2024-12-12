import Generator from "yeoman-generator";
import { Metadata, Field, TemplateData } from "./types.js";

const YO_RC_KEY_METADATA_FPATH = "metadataFilePath";
const YO_RC_KEY_DEST_ROOT_PATH = "destRootPath";

class SvqkCodeGenerator extends Generator {
  metadataFilePath: string;
  destRootPath: string;
  metadataList: Metadata[];

  constructor(args: string | string[], opts: Record<string, unknown>) {
    super(args, opts);
    this.metadataFilePath = this.config.get(YO_RC_KEY_METADATA_FPATH);
    this.destRootPath = this.config.get(YO_RC_KEY_DEST_ROOT_PATH);
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

    this.metadataList.forEach(({ packageName, className, fields }) => {
      if (!this.args.includes(className)) {
        return;
      }

      const tmplData = this._generate_template_data(
        packageName,
        className,
        fields
      );

      // Generate files for domain package
      ["Repository", "Service"].forEach((layer) => {
        const destPkgPath = this._generate_dest_package_path(
          this.destRootPath,
          tmplData.domainPkgNm
        );
        this._output_java_file(layer, destPkgPath, tmplData);
      });

      // Generate files for interfaces package
      ["Dto", "Controller"].forEach((layer) => {
        const destPkgPath = this._generate_dest_package_path(
          this.destRootPath,
          tmplData.interfacesPkgNm
        );
        this._output_java_file(layer, destPkgPath, tmplData);
      });
    });
  }

  end() {
    this.log("Completed.");
  }

  _generate_template_data(
    domainPkgNm: string,
    classNm: string,
    fields: Field[]
  ): TemplateData {
    const entityNmPascal = this._extract_entity_name(classNm);

    return {
      domainPkgNm: domainPkgNm,
      interfacesPkgNm: domainPkgNm.replace(".domain.", ".interfaces."),
      entityNmPascal: entityNmPascal,
      entityNmCamel:
        entityNmPascal.charAt(0).toLowerCase() + entityNmPascal.slice(1),
      entityNmAllCaps: entityNmPascal.toUpperCase(),
      entityIdType: fields.find((field) => field.id)?.javaType ?? "",
      fields,
    };
  }

  _output_java_file(
    layer: string,
    destPkgPath: string,
    tmplData: TemplateData
  ) {
    this.fs.copyTpl(
      this.templatePath(`java/${layer}.java`),
      this.destinationPath(
        `${destPkgPath}/${tmplData.entityNmPascal}${layer}.java`
      ),
      tmplData
    );
  }

  _extract_entity_name(entityClassNm: string): string {
    return entityClassNm.replace("Entity", "");
  }

  _generate_dest_package_path(destRootPath: string, pkgNm: string): string {
    return `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }
}

export default SvqkCodeGenerator;
