# Generator

Generator は、ソースファイルの自動生成ツールです。Generator が生成するソースファイルは、テーブルに対する CRUD 操作を実行するアプリケーション(Frontend、Backend)や自動テスト(Integration Test、End to End Test)のもので、DB 内のテーブルのメタデータをもとに生成されます。

## 使用方法

Generator はコマンドまたは VSCode タスクで実行します。

### コマンドで実行

本プロジェクトのディレクトリで以下のコマンドを実行します。

```sh
pnpm generate [options] [Table...]
```

`options`に指定可能な値は以下の通りです。

- `--component`: 生成するソースファイルの種類を指定します。指定可能な値は`backend`、`integration-test`、`api-client`、`frontend`、`e2e-test`、`all`です。指定しない場合は`all`として実行します。
- `--templateType`: ソースファイルのテンプレートを指定します。指定可能な値は`skeleton`、`arch`です。指定しない場合は本プロジェクト以下の`.yo-rc.json`の`templateType`の値が使用されます。それぞれの値の意味は以下の通りです。
  - `skeleton`: 生成されるソースファイルは、HelloTable のものと同様の構成になります。
  - `arch`: 生成されるソースファイルは、アーキテクチャ記述の定義に準拠し、arch パッケージ以下の共通機能を使用したものになります。

`Table`には CRUD 操作を行う対象の Table を指定します。複数指定する場合は、スペース区切りとします。

コマンドを実行すると、指定した`component`のソースファイルが生成されます。ソースファイルの出力先のディレクトリ、ファイル名、ソースコードは指定した`templateType`に従います。

#### 実行例

下記のコマンドは、`WorldTable`に対し CRUD を行う Backend を`skeleton`のテンプレートで生成する例です。

```sh
pnpm generate --component backend --templateType skeleton World
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

### VSCode タスクで実行

本プロジェクト、またはワークスペースを VSCode で開いた状態で VSCode タスク: `generate`を実行します。
タスクを実行すると、`component`、`Table`を入力するプロンプトが表示されます。各々の仕様は上記`コマンドで実行`を参照してください。

## カスタマイズ

Generator はカスタマイズ可能です。テンプレートをカスタマイズする場合は、`generators/app/templates`以下のファイルを変更します。ツールの挙動等を変更する場合は、`src/main`以下の`index.ts`, `types.d.ts`を変更します。

Generator は[Yeoman](https://yeoman.io/)を使用して実装されています。カスタマイズに際しては Yeoman のドキュメントを参照してください。
