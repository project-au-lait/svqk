import Generator from "yeoman-generator";
import { Metadata, Field, TemplateData } from "main/index.d";

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
        `${this.destinationRoot()}/${this.metadataFilePath}`
      ).then((module) => module.default);

      if (!this.metadataList || this.metadataList.length === 0) {
        throw new Error(
          `The meta data list on ${this.metadataFilePath} is empty.`
        );
      }
    } catch (error) {
      this.log(`Failed to read ${this.metadataFilePath}.`, error);
    }
  }

  writing() {
    const generateTemplateData = (
      domainPkgNm: string,
      classNm: string,
      fields: Field[]
    ): TemplateData => {
      const entityNmPascal = extractEntityName(classNm);

      return {
        domainPkgNm: domainPkgNm,
        interfacesPkgNm: domainPkgNm.replace(".domain.", ".interfaces."),
        entityNmPascal: entityNmPascal,
        entityNmCamel:
          entityNmPascal.charAt(0).toLowerCase() + entityNmPascal.slice(1),
        entityNmAllCaps: entityNmPascal.toUpperCase(),
        entityIdType: fields.find((field) => field.id)?.javaType ?? "",
      };
    };

    const outputJavaFile = (
      layer: string,
      destPkgPath: string,
      tmplData: TemplateData
    ) => {
      this.fs.copyTpl(
        this.templatePath(`java/${layer}.java`),
        this.destinationPath(
          `${destPkgPath}/${tmplData.entityNmPascal}${layer}.java`
        ),
        tmplData
      );
    };

    const extractEntityName = (entityClassNm: string): string =>
      entityClassNm.replace("Entity", "");

    const generateDestPackagePath = (
      destRootPath: string,
      pkgNm: string
    ): string => `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;

    this.metadataList.forEach(({ packageName, className, fields }) => {
      const tmplData = generateTemplateData(packageName, className, fields);

      // Generate files for domain package
      ["Repository", "Service"].forEach((layer) => {
        const destPkgPath = generateDestPackagePath(
          this.destRootPath,
          tmplData.domainPkgNm
        );
        outputJavaFile(layer, destPkgPath, tmplData);
      });

      // Generate files for interfaces package
      const destPkgPath = generateDestPackagePath(
        this.destRootPath,
        tmplData.interfacesPkgNm
      );
      outputJavaFile("Controller", destPkgPath, tmplData);
    });
  }

  end() {
    this.log("Completed.");
  }
}

export default SvqkCodeGenerator;
