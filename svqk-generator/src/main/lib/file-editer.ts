import { SnippetInsertionTarget, TemplateData } from "../types.js";

const LINE_BREAK = "\n";

export class FileEditer {
  constructor(
    private readonly fs: {
      copyTpl: (src: string, dest: string, data: TemplateData) => void;
      // eslint-disable-next-line @typescript-eslint/no-explicit-any
      copy: (src: string, dest: string, options?: any) => void;
      delete: (filepath: string) => void;
      read: (filepath: string) => string | null;
    }
  ) {}

  public insert_snippet(targetList: SnippetInsertionTarget[]) {
    targetList.forEach(
      ({
        templatePath,
        destinationPath,
        placeholder,
        templateData,
        checkString,
      }: SnippetInsertionTarget) => {
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
                `^.*__${placeholder}__.*${LINE_BREAK}`,
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
    );
  }
}
