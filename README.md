# SVQK

SVQK is an asset for developing web applications with SvelteKit and Quarkus.
An asset consists of:

- **Reference Implementation**
  - **Application**
    - An asset contains the source code for a web application implemented in SvelteKit + Quarkus.
    - The Web application does CRUD processing from the screen to the database tables.
    - It also includes source code for common functionality used from across the application.
  - **Test**
    - Assets include automated testing (Unit, Integration, End to End) for web applications.
  - **Build Script**
    - The asset contains a local development environment and a build script that automatically builds the execution environment.
    - The local development environment and execution environment can be constructed by executing the command several times.
    - The scripts include DB migration, automatic generation of JPA Entities, and automatic generation of Web API Clients.
- **Maven Archetype**
  - The asset contains Maven Archetype.
  - When you start a new project with assets, you create a project from this Maven Archetype.
- **Archetecture Specification**
  - A document specifying the architecture to which a reference implementation conforms.
- **Implementation Guide**
  - This document helps you understand the contents of the Architecture Specification and Reference Implementation.

This repository contains the set of materials that make up the above assets.

## Required Software

To develop applications using SVQK, you need the following software:

- Docker Desktop
- JDK v21
- Maven
- Node.js v22
- pnpm
- Git
- Visual Studio Code

## Quick Start

### Create a project

To start developing with SVQK, create a project using Maven Archetype, which SVQK provides.
Three types of archetypes are available:

- **svqk-archetype-relimpl**
  - Archetype containing all of the above reference implementations.
- **svqk-archetype-arch**
  - An Archetype that includes the common features of the above reference implementations, except those that are screen specific.
- **svqk-archetype-skeleton**
  - This Archetype contains only the reference implementation for connectivity test, excluding all common functions and screen specific ones.

To create a project from Archetype, run the following command:

- Windows (command prompt)

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

- macOS (Terminal)

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

### Running the application

Run the Backend/Frontend application using the following VSCode tasks.

- `start-back`
  - The Quarkus development server starts and the Backend application is available.
  - Typing `w` in the `TERMINAL` panel where you started Qurakus will open the application in your browser.
- `start-front`
  - The Vite development server will start and your Frontend application will be available.
  - Your browser will automatically open and display the application.

### Project structure

The above ` mvn archetype:generate ` command generates a project with the following configuration:

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
  - Project for developing Backend applications in Quarkus.
  - Because the project was created with standard Quarkus functionality, the directory structure and available functionality is the same as the standard Quarkus [Quarkus - Creating a new project](https://quarkus.io/guides/maven-tooling#project-creation).
    - Ex. You can use the usual Quarkus Maven Plugin commands such as ` mvn quarkus:dev `.
- **my-artifactid-container**
  - It is a project to set up and run containers necessary for development, such as DBMS (PostgreSQL).
  - The usual ` docker compose ` command can be used because it contains a compose. yml file.
- **my-artifactid-e2etest**
  - Project for End to End Test in Planwright.
  - Because the project was created using standard Playwright functionality, the directory structure and functionality available are the same as standard Playwright. [Playwright - Installing Playwright](https://playwright.dev/docs/intro#installing-playwright)
    - Ex. You can use the usual Playwright commands, such as  ` pnpm playwright test `.
- **my-artifactid-migration**
  - A project for DB Migration with Flyway.
  - The Flyway Maven Plugin has been set up for use.
    - Ex. You can use the usual Flyway Maven Plugin commands, such as ` mvn flyway:migrate `.
- **my-artifactid-front**
  - Project for developing Frontend applications with SvelteKit.
  - Because the project was created with standard SvelteKit functionality, the directory structure and available functionality are the same as the standard SvelteKit [SvelteKit - Creating a project](https://kit.svelte.dev/docs/creating-a-project).
    - Ex. You can use the usual SvelteKit commands, such as ` pnpm dev `.


## Document

- [Implementation Guide](https://aulait.dev/svqk/0.8/en/impl-guide/)
- [Archetecture Description](https://aulait.dev/svqk/0.8/en/arch-desc/)

## License

[Apache License 2.0](LICENSE)