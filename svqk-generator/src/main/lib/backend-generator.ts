import { TemplateData } from "../types.js";

export class BackendGenerator {
  constructor(
    private readonly fs: {
      copyTpl: (src: string, dest: string, data: TemplateData) => void;
    },
    private readonly templatePath: (...pathSegments: string[]) => string,
    private readonly destinationPath: (...pathSegments: string[]) => string
  ) {}

  public generate_backend(
    templateData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    const destBackDomainPkgPath = this.build_dest_package_path(
      destinationPath,
      templateData.domainPkgNm
    );

    const destBackIfPkgPath = this.build_dest_package_path(
      destinationPath,
      templateData.interfacesPkgNm
    );

    if (templateType === "arch" || templateType === "skeleton") {
      // Generate files for domain package
      this.output_backend_file(
        ["Repository", "Service"],
        destBackDomainPkgPath,
        templateData,
        templateType
      );

      // Generate files for interfaces package
      this.output_backend_file(
        ["Dto", "Controller"],
        destBackIfPkgPath,
        templateData,
        templateType
      );

      if (templateData.compIdFields) {
        this.output_backend_file(
          ["IdDto"],
          destBackIfPkgPath,
          templateData,
          templateType
        );
      }
    }

    if (templateType === "arch") {
      // Generate files for interfaces package
      this.output_backend_file(
        ["Factory", "SearchCriteriaDto"],
        destBackIfPkgPath,
        templateData,
        templateType
      );
    }
  }

  public generate_integrationtest(
    templateData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    const destITPkgPath = this.build_dest_package_path(
      destinationPath,
      templateData.interfacesPkgNm
    );
    this.output_backend_file(
      ["Client", "ControllerIT", "DataFactory"],
      destITPkgPath,
      templateData,
      templateType
    );
  }

  private build_dest_package_path(destRootPath: string, pkgNm: string): string {
    return `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }

  private output_backend_file(
    components: string[],
    destPkgPath: string,
    templateData: TemplateData,
    templateType: string
  ) {
    components.forEach((component) => {
      this.fs.copyTpl(
        this.templatePath(`${templateType}/back/${component}.java`),
        this.destinationPath(
          `${destPkgPath}/${templateData.entityNmPascal}${component}.java`
        ),
        templateData
      );
    });
  }
}
