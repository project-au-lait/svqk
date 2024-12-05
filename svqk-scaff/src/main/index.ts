import Generator from 'yeoman-generator';
import { Metadata, Field, EjsData as TemplateData } from './index.d';

const YO_RC_KEY_DEST_ROOT_PATH = 'destRootPath';
const YO_RC_KEY_JEG_METADATA_FPATH = 'jegMetadataFilePath';

class MyGenerator extends Generator {
    
    destRootPath: string;
    jegMetadataFilePath: string;
    metadataList: Metadata[];

    constructor(args: string | string[], opts: Record<string, unknown>) {
        super(args, opts);
        this.destRootPath = this.config.get(YO_RC_KEY_DEST_ROOT_PATH);
        this.jegMetadataFilePath = this.config.get(YO_RC_KEY_JEG_METADATA_FPATH);
        this.metadataList = [];
    }

    initializing() {
        try {
            this.metadataList = require(`${this.destinationRoot()}/${this.jegMetadataFilePath}`);
            
            if (!this.metadataList || this.metadataList.length === 0) {
                throw new Error(
                    `A meta data list on ${this.jegMetadataFilePath} is empty.`
                );
            }
        } catch (error) {
            this.log(`Failed to read ${this.jegMetadataFilePath}.`, error);
        }
    }

    writing() {
        const generateConfiguration = (domainPkgName: string, className: string, fields: Field[]): TemplateData => {
            const entNamePascal = extractEntName(className);

            return {
                domainPkgName: domainPkgName,
                interfacesPkgName: domainPkgName.replace('.domain.', '.interfaces.'),
                entNamePascal: entNamePascal,
                 entNameCamel:
                    entNamePascal.charAt(0).toLowerCase() + entNamePascal.slice(1),
                entNameAllCaps: entNamePascal.toUpperCase(),
                entIdType: fields.find(field => field.id)?.javaType??''
            };
        };
        
        const outputJavaFile = (layer: string, destPkgPath: string, data: TemplateData) => {
            this.fs.copyTpl(
                this.templatePath(`java/${layer}.java`),
                this.destinationPath(`${destPkgPath}/${data.entNamePascal}${layer}.java`),
                data
            );
        }

        const extractEntName = (entClassName: string): string => entClassName.replace('Entity', '');
        
        const generateDestPkgPath = (destRootPath: string, pkgName: string): string => `${destRootPath}/${pkgName.replace(/\./g, '/')}`;
        
        this.metadataList.forEach(({ packageName, className, fields } ) => {
            const cf = generateConfiguration(packageName, className, fields);
            
            // Generate files for domain package
            ['Repository', 'Service'].forEach(layer => {
                const destPkgPath = generateDestPkgPath(this.destRootPath, cf.domainPkgName);
                outputJavaFile(layer, destPkgPath, cf);
            });
            
            // Generate files for interfaces package
            const destPkgPath = generateDestPkgPath(
                this.destRootPath,
                cf.interfacesPkgName
            );
            outputJavaFile('Controller', destPkgPath, cf);
        });
    }
    
    end() {
        this.log('Completed.');
    }
}

export default MyGenerator;
