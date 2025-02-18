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
  idField: Field;
  compIdFields?: Field[];
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
};
