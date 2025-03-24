import { TemplateData } from "../types.js";

export class BackendGenerator {
  private readonly fs: {
    copyTpl: (src: string, dest: string, data: TemplateData) => void;
  };
  private readonly templatePath: (...pathSegments: string[]) => string;
  private readonly destinationPath: (...pathSegments: string[]) => string;

  constructor({
    fs,
    templatePath,
    destinationPath,
  }: {
    fs: { copyTpl: (src: string, dest: string, data: TemplateData) => void };
    templatePath: (...pathSegments: string[]) => string;
    destinationPath: (...pathSegments: string[]) => string;
  }) {
    this.fs = fs;
    this.templatePath = templatePath;
    this.destinationPath = destinationPath;
  }

  public generate_backend(
    tmplData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    const destBackDomainPkgPath = BackendGenerator.build_dest_package_path(
      destinationPath,
      tmplData.domainPkgNm
    );

    const destBackIfPkgPath = BackendGenerator.build_dest_package_path(
      destinationPath,
      tmplData.interfacesPkgNm
    );

    if (templateType === "arch" || templateType === "skeleton") {
      // Generate files for domain package
      this.output_backend_file(
        ["Repository", "Service"],
        destBackDomainPkgPath,
        tmplData,
        templateType
      );

      // Generate files for interfaces package
      this.output_backend_file(
        ["Dto", "Controller"],
        destBackIfPkgPath,
        tmplData,
        templateType
      );

      if (tmplData.compIdFields) {
        this.output_backend_file(
          ["IdDto"],
          destBackIfPkgPath,
          tmplData,
          templateType
        );
      }
    }

    if (templateType === "arch") {
      // Generate files for interfaces package
      this.output_backend_file(
        ["Factory", "SearchCriteriaDto"],
        destBackIfPkgPath,
        tmplData,
        templateType
      );
    }
  }

  public generate_integrationtest(
    tmplData: TemplateData,
    templateType: string,
    destinationPath: string
  ) {
    const destITPkgPath = BackendGenerator.build_dest_package_path(
      destinationPath,
      tmplData.interfacesPkgNm
    );
    this.output_backend_file(
      ["Client", "ControllerIT", "DataFactory"],
      destITPkgPath,
      tmplData,
      templateType
    );
  }

  private output_backend_file(
    components: string[],
    destPkgPath: string,
    tmplData: TemplateData,
    templateType: string
  ) {
    components.forEach((component) => {
      this.fs.copyTpl(
        this.templatePath(`${templateType}/back/${component}.java`),
        this.destinationPath(
          `${destPkgPath}/${tmplData.entityNmPascal}${component}.java`
        ),
        tmplData
      );
    });
  }

  private static build_dest_package_path(
    destRootPath: string,
    pkgNm: string
  ): string {
    return `${destRootPath}/${pkgNm.replace(/\./g, "/")}`;
  }
}
