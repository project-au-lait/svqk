import pluralize from "pluralize";
import { Field, Metadata, MetadataConfig, TemplateData } from "../types.js";
import url from "node:url";

export class GeneratorUtils {
  static async load_json_file(filePath: string) {
    try {
      const module = await import(`${url.pathToFileURL(filePath).href}?t=${Date.now()}`, {
        with: { type: "json" },
      });
      return module.default;
    } catch {
      return null;
    }
  }

  public static build_template_data(
    metadata: Metadata,
    metadataConfig: MetadataConfig
  ): TemplateData {
    const entityNmPascal = this.extract_entity_name(metadata.className);
    const idField = metadata.fields.find((field) => field.id) ?? ({} as Field);

    this.set_field_pascal_name(metadata.fields);

    return {
      domainPkgNm: metadata.packageName,
      interfacesPkgNm: metadata.packageName.replace(".domain.", ".interfaces."),
      entityNmPascal: entityNmPascal,
      entityNmCamel: this.pascal_to_camel(entityNmPascal),
      entityNmAllCaps: entityNmPascal.toUpperCase(),
      entityNmPlural: pluralize(entityNmPascal.toLowerCase()),
      entityNmKebab: this.pascal_to_kebab(entityNmPascal),
      entityNmFirstLetter: entityNmPascal.charAt(0).toLowerCase(),
      fields: metadata.fields,
      idField: idField,
      nonIdFields: metadata.fields.filter((field) => !field.id),
      compIdFields: this.get_composite_id_fields(idField, metadataConfig),
    };
  }

  private static extract_entity_name(entityClassNm: string): string {
    return entityClassNm.replace("Entity", "");
  }

  private static set_field_pascal_name(fields: Field[]) {
    fields.forEach((field) => {
      field.fieldNmPascal = this.camel_to_pascal(field.fieldName);
    });
  }

  private static pascal_to_camel(pascal: string): string {
    return pascal.charAt(0).toLowerCase() + pascal.slice(1);
  }

  private static pascal_to_kebab(pascal: string): string {
    return pascal
      .replace(/([a-z])([A-Z])/g, "$1-$2")
      .replace(/[A-Z]/g, (letter) => letter.toLowerCase());
  }

  private static get_composite_id_fields(
    idField: Field,
    metadataConfig: MetadataConfig
  ): Field[] | undefined {
    const compIdFields = metadataConfig.list.find(
      (meta) => meta.className === idField.javaType
    )?.fields;

    if (compIdFields) {
      this.set_field_pascal_name(compIdFields);
    }

    return compIdFields;
  }

  private static camel_to_pascal(camel: string): string {
    return camel.charAt(0).toUpperCase() + camel.slice(1);
  }
}
