type Metadata = {
  packageName: string;
  className: string;
  fields: Field[];
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
  entityIdType: string;
  idFieldName: string;
  fields: Field[];
};

declare module "**/jeg-metadata.json" {
  const data: Metadata[];
  export default data;
}

export { Metadata, Field, TemplateData };
