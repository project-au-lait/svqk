type Metadata = {
  packageName: string;
  className: string;
  fields: Field[];
}

type Field = {
  fieldName: string;
  javaType: string;
  multiple: Boolean;
  dbType: string;
  dbTypeSize: number;
  id: Boolean;
  required: Boolean;
}

type TemplateData = {
    domainPkgNm: string;
    interfacesPkgNm: string;
    entityNmPascal: string;
    entityNmCamel: string;
    entityNmAllCaps: string;
    entityIdType: string;
}

declare module '**/jeg-metadata.json' {
    const data: Metadata[];
    export default data;
}

export { Metadata, Field, TemplateData };
