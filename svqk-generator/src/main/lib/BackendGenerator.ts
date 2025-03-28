import { Fs, TemplateData, DestPaths } from "../types.js";

export class BackendGenerator {
  constructor(
    private readonly fs: Fs,
    private readonly templatePath: (...pathSegments: string[]) => string,
    private readonly destinationPath: (...pathSegments: string[]) => string,
    private readonly templateType: string,
    private readonly destPaths: DestPaths
  ) {}

  public generate_backend(templateData: TemplateData) {
    const destBackDomainPkgPath = this.build_dest_package_path(
      this.destPaths.destBackPath,
      templateData.domainPkgNm
    );

    const destBackIfPkgPath = this.build_dest_package_path(
      this.destPaths.destBackPath,
      templateData.interfacesPkgNm
    );

    if (this.templateType === "arch" || this.templateType === "skeleton") {
      // Generate files for domain package
      this.output_backend_file(
        ["Repository", "Service"],
        destBackDomainPkgPath,
        templateData,
        this.templateType
      );

      // Generate files for interfaces package
      this.output_backend_file(
        ["Dto", "Controller"],
        destBackIfPkgPath,
        templateData,
        this.templateType
      );

      if (templateData.compIdFields) {
        this.output_backend_file(
          ["IdDto"],
          destBackIfPkgPath,
          templateData,
          this.templateType
        );
      }
    }

    if (this.templateType === "arch") {
      // Generate files for interfaces package
      this.output_backend_file(
        ["Factory", "SearchCriteriaDto"],
        destBackIfPkgPath,
        templateData,
        this.templateType
      );
    }
  }

  public generate_integrationtest(templateData: TemplateData) {
    const destITPkgPath = this.build_dest_package_path(
      this.destPaths.destITPath,
      templateData.interfacesPkgNm
    );
    this.output_backend_file(
      ["Client", "ControllerIT", "DataFactory"],
      destITPkgPath,
      templateData,
      this.templateType
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
        this.templatePath(`${templateType}/backend/${component}.java`),
        this.destinationPath(
          `${destPkgPath}/${templateData.entityNmPascal}${component}.java`
        ),
        templateData
      );
    });
  }
}
