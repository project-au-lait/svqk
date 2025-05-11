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
  entityNmFirstLetter: string;
  fields: Field[];
  idField: Field;
  nonIdFields: Field[];
  compIdFields?: Field[];
};

type DestPaths = {
  destBackPath: string;
  destITPath: string;
  destFrontPath: string;
  destE2EPath: string;
};

type Fs = {
  copyTpl: (src: string, dest: string, data: TemplateData) => void;
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  copy: (src: string, dest: string, options?: any) => void;
  delete: (filepath: string) => void;
  read: (filepath: string) => string | null;
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
  DestPaths,
  Fs,
};
