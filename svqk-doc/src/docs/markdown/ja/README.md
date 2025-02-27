# SVQK

SVQK は、SvelteKit と Quarkus で Web アプリケーションを開発するためのアセットです。
アセットは以下のもので構成されます。

- **リファレンス実装**
  - **アプリケーション**
    - アセットは SvelteKit + Quarkus で実装された Web アプリケーションのソースコードを含みます。
    - Web アプリケーションは画面からデータベースのテーブルに CRUD 処理を行います。
    - アプリケーション全体から使用される共通機能のソースコードも含みます。
  - **テスト**
    - アセットは Web アプリケーションの自動テスト(Unit、Integration、End to End)を含みます。
  - **ビルドスクリプト**
    - アセットはローカル開発環境、及び実行環境を自動で構築するビルドスクリプトを含みます。
    - 数回のコマンド実行でローカル開発環境、及び実行環境を構築できます。
    - スクリプトは DB マイグレーション、JPA Entity の自動生成、Web API Client の自動生成を含みます。
- **Maven Archetype**
  - アセットは Maven Archetype を含みます。
  - アセットを使用して新しくプロジェクトを開始する際は、この Maven Archetype からプロジェクトを作成します。
- **アーキテクチャ記述**
  - リファレンス実装が準拠するアーキテクチャを規定した文書です。
- **実装ガイド**
  - アーキテクチャ記述とリファレンス実装の内容の理解を助けるための文書です。

このリポジトリは、上記アセットを構成する資材一式を格納しています。

## 必要なソフトウェア

SVQK を使用してアプリケーションを開発するには、以下のソフトウェアが必要です。

- Docker Desktop
- JDK v21
- Maven
- Node.js v22
- pnpm
- Git
- Visual Studio Code

## クイックスタート

### プロジェクトの作成

SVQK を使用して開発を始めるには、SVQK が提供する Maven Archetype を使用してプロジェクトを作成します。
以下の 3 種類の Archetype が使用できます。

- **svqk-archetype-relimpl**
  - 上記のリファレンス実装を全て含んだ Archetype です。
- **svqk-archetype-arch**
  - 上記のリファレンス実装の内、画面固有のものを除き、共通機能のものを含んだ Archetype です。
- **svqk-archetype-skeleton**
  - 上記のリファレンス実装の内、共通機能・画面固有のものを全て除き、疎通確認用のもののみを含んだ Archetype です。

Archetype からプロジェクトを作成するには、以下のコマンドを実行します。

- Windows (コマンドプロンプト)

```sh
chcp 65001

mvn archetype:generate ^
    -DarchetypeGroupId=dev.aulait.svqk ^
    -DarchetypeArtifactId=svqk-archetype-refimpl ^
    -DarchetypeVersion=0.8 ^
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
    -DarchetypeVersion=0.8 \
    -DgroupId=my.group.id \
    -DartifactId=my-artifactid \
    -Dversion=1.0-SNAPSHOT

cd my-artifactid

chmod u+x mvnw

./mvnw install -T 1C -P setup,browse-e2etest

code my-artifactid.code-workspace
```

### アプリケーションの実行

以下の VSCode タスク使用して Backend・Frontend アプリケーションを実行します。

- `start-backend`
  - Quarkus の開発サーバーが起動し、Backend アプリケーションが使用可能になります。
  - Qurakus を起動した`TERMINAL`パネルで`w`をタイプすると、アプリケーションをブラウザで開きます。
- `start-frontend`
  - Vite の開発サーバーが起動し、Frontend アプリケーションが使用可能になります。
  - 自動でブラウザが開き、アプリケーションが表示されます。

### プロジェクトの構成

上記の `mvn archetype:generate` コマンドを実行すると、以下の構成でプロジェクトが生成されます。

```txt
📁 my-artifactid
├── 📁 my-artifactid-backend  <------ Quarkus (Maven)
│   └── 📄 pom.xml
├── 📁 my-artifactid-container  <- Docker
│   ├── 📄 compose.yml
│   └── 📄 pom.xml
├── 📁 my-artifactid-e2etest  <--- Playwright (pnpm)
│   ├── 📄 package.json
│   └── 📄 pom.xml
├── 📁 my-artifactid-frontend  <----- SvelteKit (pnpm)
│   ├── 📄 package.json
│   └── 📄 pom.xml
├── 📁 my-artifactid-migration  <- Flyway (Maven)
│   └── 📄 pom.xml
└── 📄 pom.xml
```

- **my-artifactid-backend**
  - Quarkus で Backend アプリケーションを開発するためのプロジェクトです。
  - Quarkus の標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能は Quarkus の標準のものと同じです。[Quarkus - Creating a new project](https://quarkus.io/guides/maven-tooling#project-creation)
    - Ex. `mvn quarkus:dev` 等、通常の Quarkus Maven Plugin のコマンドが使用できます。
- **my-artifactid-container**
  - DBMS(PostgreSQL)など、開発に必要なコンテナを設定・実行するためのプロジェクトです。
  - compose.yml ファイルが格納されているため、通常の `docker compose` コマンドが使用できます。
- **my-artifactid-e2etest**
  - Planwright で End to End Test を行うためのプロジェクトです。
  - Playwright の標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能は Playwright の標準のものと同じです。[Playwright - Installing Playwright](https://playwright.dev/docs/intro#installing-playwright)
    - Ex. `pnpm playwright test` 等、通常の Playwright のコマンドが使用できます。
- **my-artifactid-migration**
  - Flyway で DB Migration を行うためのプロジェクトです。
  - Flyway Maven Plugin が使用可能な状態でセットアップされています。
    - Ex. `mvn flyway:migrate` 等、通常の Flyway Maven Plugin のコマンドが使用できます。
- **my-artifactid-frontend**
  - SvelteKit で Frontend アプリケーションを開発するためのプロジェクトです。
  - SvelteKit の標準機能で作成したプロジェクトのため、ディレクトリ構成や使用できる機能は SvelteKit の標準のものと同じです。[SvelteKit - Creating a project](https://kit.svelte.dev/docs/creating-a-project)
    - Ex. `pnpm dev` 等、通常の SvelteKit のコマンドが使用できます。

## ドキュメント

- [実装ガイド](https://aulait.dev/svqk/0.8/en/impl-guide/)
- [アーキテクチャ記述](https://aulait.dev/svqk/0.8/en/arch-desc/)

## ライセンス

[Apache License 2.0](LICENSE)
