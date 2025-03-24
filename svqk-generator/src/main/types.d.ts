import type { GeneratorOptions } from "@yeoman/types";

type CustomOptions = GeneratorOptions & {
  component: string;
  templateType: string;
};

type OptionsValues = {
  component: string;
  templateType: string;
};

type Metadata = {
  packageName: string;
  className: string;
  tableName: string;
  fields: Field[];
};

type MetadataConfig = {
  filePath: string;
  list: Metadata[];
};

type GenApiClientConfig = {
  genOpenApiJsonCmd: string;
  frontApiClientPath: string;
  e2eApiClientPath: string;
};

type Field = {
  fieldName: string;
  fieldNmPascal?: string;
  javaType: string;
  multiple: boolean;
  stringLength: number;
  id: boolean;
  required: boolean;
};

type TemplateData = {
  domainPkgNm: string;
  interfacesPkgNm: string;
  entityNmPascal: string;
  entityNmCamel: string;
  entityNmAllCaps: string;
  entityNmPlural: string;
  entityNmKebab: string;
  fields: Field[];
  idField: Field;
  nonIdFields: Field[];
  compIdFields?: Field[];
};

type GenerateTarget = {
  templatePath: string;
  destinationPath: string;
  templateData: TemplateData;
};

type SnippetInsertionTarget = {
  filePath: string;
  checkString: string;
  placeholder: string;
  rawTextList: string[];
};

type DestPaths = {
  destBackPath: string;
  destITPath: string;
  destFrontPath: string;
  destE2EPath: string;
};

declare module "**/jeg-metadata.json" {
  const data: Metadata[];
  export default data;
}

export {
  CustomOptions,
  OptionsValues,
  Metadata,
  MetadataConfig,
  GenApiClientConfig,
  Field,
  TemplateData,
  GenerateTarget,
  SnippetInsertionTarget,
  DestPaths,
};
