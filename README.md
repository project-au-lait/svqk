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

## Website

[aulait.dev/#svqk](https://aulait.dev/#svqk)

## Document

- [Implementation Guide](https://aulait.dev/svqk/0.8/en/impl-guide/)
- [Archetecture Description](https://aulait.dev/svqk/0.8/en/arch-desc/)

## License

[Apache License 2.0](LICENSE)
