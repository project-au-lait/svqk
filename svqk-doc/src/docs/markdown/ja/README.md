# SVQK

SVQKは、SvelteKitとQuarkusでWebアプリケーションを開発するためのアセットです。
アセットは以下のもので構成されます。

- **リファレンス実装**
  - **アプリケーション**
    - アセットはSvelteKit + Quarkusで実装されたWebアプリケーションのソースコードを含みます。
    - Webアプリケーションは画面からデータベースのテーブルにCRUD処理を行います。
    - アプリケーション全体から使用される共通機能のソースコードも含みます。
  - **テスト**
    - アセットはWebアプリケーションの自動テスト(Unit、Integration、End to End)を含みます。
  - **ビルドスクリプト**
    - アセットはローカル開発環境、及び実行環境を自動で構築するビルドスクリプトを含みます。
    - 数回のコマンド実行でローカル開発環境、及び実行環境を構築できます。
    - スクリプトはDBマイグレーション、JPA Entityの自動生成、Web API Clientの自動生成を含みます。
- **Maven Archetype**
  - アセットはMaven Archetypeを含みます。
  - アセットを使用して新しくプロジェクトを開始する際は、このMaven Archetypeからプロジェクトを作成します。
- **アーキテクチャ仕様書**
  - リファレンス実装が準拠するアーキテクチャを規定した文書です。
- **実装ガイド**
  - アーキテクチャ仕様とリファレンス実装の内容の理解を助けるための文書です。

このリポジトリは、上記アセットを構成する資材一式を格納しています。

## 必要なソフトウェア

SVQKを使用してアプリケーションを開発するには、以下のソフトウェアが必要です。

- Docker Desktop
- JDK v21
- Node.js v20
- pnpm
- Git
- Visual Studio Code

## クイックスタート

### プロジェクトの作成

SVQKを使用して開発を始めるには、SVQKが提供するMaven Archetypeを使用してプロジェクトを作成します。
以下の3種類の Archetypeが使用できます。

- **svqk-archetype-relimpl**
  - 上記のリファレンス実装を全て含んだ Archetypeです。
- **svqk-archetype-arch**
  - 上記のリファレンス実装の内、画面固有のものを除き、共通機能のものを含んだ Archetypeです。
- **svqk-archetype-skeleton**
  - 上記のリファレンス実装の内、共通機能・画面固有のものを全て除き、疎通確認用のもののみを含んだ Archetypeです。

Archetypeからプロジェクトを作成するには、以下のコマンドを実行します。

- Windows (コマンドプロンプト)

```sh
mvn archetype:generate ^
    -DarchetypeGroupId=dev.aulait.svqk ^
    -DarchetypeArtifactId=svqk-archetype-refimpl ^
    -DarchetypeVersion=0.6-SNAPSHOT ^
    -DgroupId=my.group.id ^
    -DartifactId=my-artifactid ^
    -Dversion=1.0-SNAPSHOT

cd my-artifactid

mvnw install -T 1C -P setup,browse-e2etest

code my-artifactid.code-workspace
```

- macOS (ターミナル)

```sh
mvn archetype:generate  \
    -DarchetypeGroupId=dev.aulait.svqk \
    -DarchetypeArtifactId=svqk-archetype-refimpl \
    -DarchetypeVersion=0.6 \
    -DgroupId=my.group.id \
    -DartifactId=my-artifactid \
    -Dversion=1.0-SNAPSHOT

cd my-artifactid

chmod u+x mvnw

./mvnw install -T 1C -P setup,browse-e2etest

code my-artifactid.code-workspace
```

### アプリケーションの実行

以下のVSCodeタスク使用してBackend・Frontendアプリケーションを実行します。

- `start-back`
  - Quarkusの開発サーバーが起動し、Backendアプリケーションが使用可能になります。
  - Qurakusを起動した`TERMINAL`パネルで`w`をタイプすると、アプリケーションをブラウザで開きます。
- `start-front`
  - Viteの開発サーバーが起動し、Frontendアプリケーションが使用可能になります。
  - 自動でブラウザが開き、アプリケーションが表示されます。

### プロジェクトの構成

上記の `mvn archetype:generate` コマンドを実行すると、以下の構成でプロジェクトが生成されます。

```txt
📁 my-artifactid
├── 📁 my-artifactid-back  <------ Quarkus (Maven)
│   └── 📄 pom.xml
├── 📁 my-artifactid-container  <- Docker
│   ├── 📄 compose.yml
│   └── 📄 pom.xml
├── 📁 my-artifactid-e2etest  <--- Playwright (pnpm)
│   ├── 📄 package.json
│   └── 📄 pom.xml
├── 📁 my-artifactid-front  <----- SvelteKit (pnpm)
│   ├── 📄 package.json
│   └── 📄 pom.xml
├── 📁 my-artifactid-migration  <- Flyway (Maven)
│   └── 📄 pom.xml
└── 📄 pom.xml
```

- **my-artifactid-back**
  - QuarkusでBackendアプリケーションを開発するためのプロジェクトです。
  - Quarkusの標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能はQuarkusの標準のものと同じです。[Quarkus - Creating a new project](https://quarkus.io/guides/maven-tooling#project-creation)
    - Ex. `mvn quarkus:dev` 等、通常のQuarkus Maven Pluginのコマンドが使用できます。  
- **my-artifactid-container**
  - DBMS(PostgreSQL)など、開発に必要なコンテナを設定・実行するためのプロジェクトです。
  - compose.ymlファイルが格納されているため、通常の `docker compose` コマンドが使用できます。
- **my-artifactid-e2etest**
  - PlanwrightでEnd to End Testを行うためのプロジェクトです。
  - Playwrightの標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能はPlaywrightの標準のものと同じです。[Playwright - Installing Playwright](https://playwright.dev/docs/intro#installing-playwright)
    - Ex. `pnpm playwright test` 等、通常のPlaywrightのコマンドが使用できます。
- **my-artifactid-migration**
  - FlywayでDB Migrationを行うためのプロジェクトです。
  - Flyway Maven Pluginが使用可能な状態でセットアップされています。
    - Ex. `mvn flyway:migrate` 等、通常のFlyway Maven Pluginのコマンドが使用できます。
- **my-artifactid-front**
  - SvelteKitでFrontendアプリケーションを開発するためのプロジェクトです。
  - SvelteKitの標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能はSvelteKitの標準のものと同じです。[SvelteKit - Creating a project](https://kit.svelte.dev/docs/creating-a-project)
    - Ex. `pnpm dev` 等、通常のSvelteKitのコマンドが使用できます。


## ドキュメント

- [実装ガイド](http://sitoolkit-dev.monocrea.co.jp/gitbucket/project-au-lait/svqk/pages/0.6/ja/impl-guide/index.html)
- [アーキテクチャ仕様書](http://sitoolkit-dev.monocrea.co.jp/gitbucket/project-au-lait/svqk/pages/0.6/ja/arch-spec/index.html)

## ライセンス

[Apache License 2.0](LICENSE)
