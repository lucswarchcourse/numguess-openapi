# Number Guessing Game - API First

## Background

This project started from a very old example built with the Restlet library. The [original code](https://github.com/LoyolaChicagoCode/numguess-restlet-spring-java) was primarily an educational artifact for introducing REST and HATEOAS concepts.

## How we recovered the API spec

We used Claude to reverse‑engineer the OpenAPI description from the legacy Restlet example. The resulting specification lives in `openapi.yaml` at the repo root and now reflects:

- A root (`/`) entrypoint with guidance to link or redirect to `/games`
- A games collection at `/games` that issues UUIDs on creation
- Game instance operations at `/games/{uuid}` (including guess submission and deletion)

## API‑first approach

We’re trying to take an API‑first approach:

- Treat `openapi.yaml` as the single source of truth for the service contract
- Evolve behavior in the spec first; generate and/or implement code from it
- Keep clients, docs, and tests aligned via code generation and examples derived from the spec

The API documentation is generated using `redocly` and available [here](doc/redoc-static.html).

## Where we’re headed next

We’re going to experiment with OpenAPI Generator to build a modern web service/app from this spec. The goal is to regenerate a clean, maintainable implementation (e.g., Spring Boot, Node/Express, or another stack) directly from `openapi.yaml`, and then iterate on the design and code together.

## Status

This is the beginning of the new journey. The legacy Restlet code served as a blueprint, the OpenAPI spec is our current source of truth, and the next steps will focus on code generation and modernization around that spec.

