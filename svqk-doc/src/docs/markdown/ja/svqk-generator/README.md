# Generator

Genertorは、ソースファイルの自動生成ツールです。Generatorが生成するソースファイルは、DB内のテーブルのメタデータをもとに、テーブルに対するCRUD操作を実行するアプリケーション(Frontend、Backend)や自動テスト(Integration Test、End to End Test)のものです。

## 使用方法

GeneratorはコマンドまたはVSCodeタスクで実行します。

### コマンドで実行

本プロジェクトのディレクトリで以下のコマンドを実行します。

```sh
pnpm start [options] [Entity...]
```

`options`に指定可能な値は以下の通りです。

- `--component`: 生成するソースファイルの種類を指定します。指定可能な値は`backend`、`integration-test`、`api-client`、`frontend`、`e2e-test`、`all`です。指定しない場合は`all`として実行します。
- `--templateType`: ソースファイルのテンプレートを指定します。指定可能な値は`skeleton`、`arch`です。指定しない場合は本プロジェクト以下の`.yo-rc.json`の`templateType`の値が使用されます。それぞれの値の意味は以下の通りです。
  - `skeleton`: 生成されるソースファイルは、
  - `arch`: 生成されるソースファイルは、アーキテクチャ記述の定義に準拠し、archパッケージ以下の共通機能を使用したものになります。

`Entity`にはCRUD操作を行う対象のEntityを指定します。複数指定する場合は、スペース区切りとします。

コマンドを実行すると、指定した`component`のソースファイルが生成されます。ソースファイルの出力先のディレクトリ、ファイル名、ソースコードは指定した`templateType`に従います。

#### 実行例

下記のコマンドは、`WorldEntity`に対しCRUDを行うBackendを`skeleton`のテンプレートで生成する例です。

```sh
pnpm start --component backend --templateType skeleton World
```

上記のコマンドを実行すると、下記の通りソースファイルが生成されます。

```txt
📁 my-artifactid-backend/src/main/java/my/group/id
  ├── 📁 domain/hello
  │   ├── 📄 WorldService.java
  │   └── 📄 WorldRepository.java
  └── 📁 interfaces/hello
      └── 📄 WorldController.java
```

### VSCodeタスクで実行

本プロジェクト、またはワークスペースをVSCodeで開いた状態でVSCodeタスク: `generate`を実行します。
タスクを実行すると、`component`、`Entity`を入力するプロンプトが表示されます。各々の仕様は上記`コマンドで実行`を参照してください。

## カスタマイズ

Generatorはカスタマイズ可能です。テンプレートをカスタマイズする場合は、`generators/app/templates`以下のファイルを変更します。ツールの挙動等を変更する場合は、`src/main`以下の`index.ts`, `types.d.ts`を変更します。

Generatorは[Yeoman](https://yeoman.io/)を使用して実装されています。カスタマイズに際してはYeomanのドキュメントを参照してください。