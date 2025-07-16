import { Fs, TemplateData } from "../types.js";

const LINE_BREAK_PATTERN = "\r?\n";
const LINE_BREAK = "\n";

export class FileEditor {
  constructor(private readonly fs: Fs) {}

  public insert_snippet(
    templatePath: string,
    destinationPath: string,
    placeholder: string,
    templateData: TemplateData,
    checkString: string
  ) {
    const renderedPath = templatePath.replace(".ejs", ".txt");
    this.fs.copyTpl(templatePath, renderedPath, templateData);
    const renderedText = this.fs.read(renderedPath);

    if (renderedText) {
      this.fs.copy(destinationPath, destinationPath, {
        process: function (content: string) {
          if (content.includes(checkString)) {
            return content;
          }

          const originalText = content.toString();
          const regex = new RegExp(
            `^.*__${placeholder}__.*${LINE_BREAK_PATTERN}`,
            "m"
          );
          const placeholderLine = RegExp(regex).exec(originalText);
          return originalText.replace(
            regex,
            renderedText + LINE_BREAK + placeholderLine
          );
        },
      });
    }

    this.fs.delete(renderedPath);
  }
}
