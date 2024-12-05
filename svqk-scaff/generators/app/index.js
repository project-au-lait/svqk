"use strict";
const Generator = require("yeoman-generator");

// Type Information: jegMetadata
// type MetadataList = Metadata[];
// type Metadata = {
//   packageName: string;
//   className: string;
//   fields: Field[];
// }
// type Field = {
//   fieldName: string;
//   javaType: string;
//   multiple: Boolean;
//   dbType: strict;
//   dbTypeSize: int;
//   id: Boolean;
//   required: Boolean;
// }

const ENV_KEY_DEST_ROOT_PATH = "destRootPath";
const ENV_KEY_JEG_METADATA_FPATH = "jegMetadataFilePath";

module.exports = class extends Generator {
  initializing() {
    this.jegMetadataFilePath = this.config.get(ENV_KEY_JEG_METADATA_FPATH);

    try {
      this.metadataList = this.fs.readJSON(this.jegMetadataFilePath);

      if (!this.metadataList || this.metadataList.length === 0) {
        throw new Error(
          `A meta data list on ${this.jegMetadataFilePath} is empty.`
        );
      }
    } catch (error) {
      this.log(`Failed to read ${this.jegMetadataFilePath}.`, error);
    }
  }

  writing() {
    const generateConfiguration = (domainPkgName, className, fields) => {
      return {
        domainPkgName: domainPkgName,
        interfacesPkgName: domainPkgName.replace(".domain.", ".interfaces."),
        entNamePascal: extractEntName(className),
        entIdType: fields.find(field => field.id)?.javaType
      };
    };

    const outputJavaFile = (layer, destPkgPath, cf) => {
      const ejsData = {
        domainPkgName: cf.domainPkgName,
        interfacesPkgName: cf.interfacesPkgName,
        entNamePascal: cf.entNamePascal,
        entNameCamel:
          cf.entNamePascal.charAt(0).toLowerCase() + cf.entNamePascal.slice(1),
        entNameAllCaps: cf.entNamePascal.toUpperCase(),
        entIdType: cf.entIdType
      };

      this.fs.copyTpl(
        this.templatePath(`java/${layer}.java`),
        this.destinationPath(`${destPkgPath}/${cf.entNamePascal}${layer}.java`),
        ejsData
      );
    };

    const extractEntName = entClassName => entClassName.replace("Entity", "");

    const generateDestPkgPath = (destRootPath, pkgName) =>
      `${destRootPath}/${pkgName.replaceAll(".", "/")}`;

    this.metadataList.forEach(metadata => {
      const { packageName, className, fields } = metadata;

      const destRootPath = this.config.get(ENV_KEY_DEST_ROOT_PATH);
      const cf = generateConfiguration(packageName, className, fields);

      // Generating for domain package
      ["Repository", "Service"].forEach(layer => {
        const destPkgPath = generateDestPkgPath(destRootPath, cf.domainPkgName);
        outputJavaFile(layer, destPkgPath, cf);
      });

      // Generating for interfaces package
      const destPkgPath = generateDestPkgPath(
        destRootPath,
        cf.interfacesPkgName
      );
      outputJavaFile("Controller", destPkgPath, cf);
    });
  }

  end() {
    this.log("");
  }
};
