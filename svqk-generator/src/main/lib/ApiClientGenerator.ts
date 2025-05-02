import fs from "fs";
import path from "node:path";
import { generateApi } from "swagger-typescript-api";
import { GenApiClientConfig } from "../types.js";
import { ExternalCommandExecutor } from "./ExternalCommandExecuter.js";

export class ApiClientGenerator {
  static exec(genApiClientConfig: GenApiClientConfig) {
    ExternalCommandExecutor.exec(genApiClientConfig.genOpenApiJsonCmd);
    this.exec_gen_api_client(
      genApiClientConfig.frontApiClientPath,
      genApiClientConfig.e2eApiClientPath
    );
  }

  private static exec_gen_api_client(
    frontApiClientPath: string,
    e2eApiClientPath: string
  ) {
    const openApiJsonPath = path.resolve(
      process.cwd(),
      "./target/openapi.json"
    );
    console.log("openapi.json Path:", openApiJsonPath);
    if (!fs.existsSync(openApiJsonPath)) {
      throw new Error("openapi.json is not found.");
    }

    generateApi({
      name: "Api.ts",
      input: openApiJsonPath,
      output: path.resolve(process.cwd(), frontApiClientPath),
      moduleNameIndex: 1,
      hooks: {
        onFormatRouteName: (routeInfo) => {
          return routeInfo.operationId;
        },
        onFormatTypeName: (typeName, rawTypeName, schemaType) => {
          if (schemaType === "type-name" && typeName.endsWith("Dto")) {
            const newTypeName = typeName.replace(/Dto$/, "Model");
            console.log(`Replace typeName ${typeName} to ${newTypeName}`);
            return newTypeName;
          }

          return typeName;
        },
      },
    })
      .then(() => {
        this.exec_copy_api(frontApiClientPath, e2eApiClientPath);
        console.log("Client API generated successfully!");
      })
      .catch((err) => {
        throw new Error(`Error generating Client API: ${err}`);
      });
  }

  private static exec_copy_api(
    frontApiClientPath: string,
    e2eApiClientPath: string
  ) {
    fs.copyFile(
      `${frontApiClientPath}/Api.ts`,
      `${e2eApiClientPath}/Api.ts`,
      (err) => {
        if (err) {
          throw new Error(`Copy failed: ${err}`);
        } else {
          console.log("Files copied successfully.");
        }
      }
    );
  }
}
