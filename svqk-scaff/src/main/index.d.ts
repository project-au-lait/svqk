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
  dbTypeSize: string;
  id: Boolean;
  required: Boolean;
}

type TemplateData = {
    domainPkgName: string;
    interfacesPkgName: string;
    entNamePascal: string;
    entNameCamel: string;
    entNameAllCaps: string;
    entIdType: string;
}

declare module '**/jeg-metadata.json' {
    const data: Metadata[];
    export default data;
}

export { Metadata, Field, TemplateData as EjsData };
