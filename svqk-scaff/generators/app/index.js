"use strict";
const Generator = require("yeoman-generator");

// TODO: for TypeScript
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

const JEG_META_FPATH = "metadata/jeg-metadata.json";
const LAYERS_IN_DOMAIN_PKG = ["Repository", "Service"];
const LAYERS_IN_INTERFACES_PKG = ["Controller"];
const ENV_KEY_DEST_ROOT_PATH = "destRootPath";

module.exports = class extends Generator {
  initializing() {
    try {
      this.metadataList = this.fs.readJSON(this.templatePath(JEG_META_FPATH), {
        readonly: true
      });

      if (!this.metadataList || this.metadataList.length === 0) {
        throw new Error(`Meta data list on ${JEG_META_FPATH} is empty.`);
      }
    } catch (error) {
      this.log(`Failed to read ${JEG_META_FPATH}.`, error);
    }
  }

  writing() {
    const outputJavaFile = (pkgName, entNamePascal, entIdType, layer) => {
      const destRootPath = this.config.get(ENV_KEY_DEST_ROOT_PATH);
      const destPkgPath = `${destRootPath}/${pkgName.replaceAll(".", "/")}`;
      const ejsData = {
        pkgName: pkgName,
        entNamePascal: entNamePascal,
        entNameCamel:
          entNamePascal.charAt(0).toLowerCase() + entNamePascal.slice(1),
        entNameAllCaps: entNamePascal.toUpperCase(),
        entIdType: entIdType
      };

      this.fs.copyTpl(
        this.templatePath(`java/${layer}.java`),
        this.destinationPath(`${destPkgPath}/${entNamePascal}${layer}.java`),
        ejsData
      );
    };

    const extractEntName = entClassName => entClassName.replace("Entity", "");

    const convPkgNameToInterfaces = pkgName =>
      pkgName.replace(".domain.", ".interfaces.");

    this.metadataList.forEach(metadata => {
      const { packageName, className, fields } = metadata;

      const entNamePascal = extractEntName(className);
      const entIdType = fields.find(field => field.id)?.javaType;

      // For domain package
      LAYERS_IN_DOMAIN_PKG.forEach(layer =>
        outputJavaFile(packageName, entNamePascal, entIdType, layer)
      );

      // For interfaces package
      LAYERS_IN_INTERFACES_PKG.forEach(layer => {
        const interfacesPkgName = convPkgNameToInterfaces(packageName);
        outputJavaFile(interfacesPkgName, entNamePascal, entIdType, layer);
      });
    });
  }

  end() {
    this.log("");
  }
};
