import type { GeneratorOptions } from "@yeoman/types";

type CustomOptions = GeneratorOptions & {
  component: string;
  templateType: string;
};

type OptionsInfo = {
  component: string;
  templateType: string;
};

type Metadata = {
  packageName: string;
  className: string;
  fields: Field[];
};

type MetadataInfo = {
  filePath: string;
  list: Metadata[];
};

type GenApiClientInfo = {
  genOpenApiJsonCmd: string;
  frontApiClientPath: string;
  e2eApiClientPath: string;
};

type Field = {
  fieldName: string;
  javaType: string;
  multiple: boolean;
  dbType: string;
  dbTypeSize: number;
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
  fields: Field[];
  idFieldNmPascal: string;
  idJavaType: string;
};

declare module "**/jeg-metadata.json" {
  const data: Metadata[];
  export default data;
}

export {
  CustomOptions,
  OptionsInfo,
  Metadata,
  MetadataInfo,
  GenApiClientInfo,
  Field,
  TemplateData,
};
