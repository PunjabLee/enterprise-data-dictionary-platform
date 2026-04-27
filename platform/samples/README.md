# Sample Data

This directory contains local demo fixtures for manual import tests, API smoke tests, and documentation examples.

The files are not mounted into PostgreSQL startup and are not Flyway migrations. Database schema and import behavior are owned by separate workstreams, so these fixtures remain schema-neutral until those contracts are finalized.

## Directory Layout

```text
platform/samples/
  local-demo/
    systems.csv
    data-assets.csv
    business-terms.csv
    lineage-edges.csv
```

## Usage

Use these files as stable local fixtures when testing:

- system and owner catalog imports
- database, schema, table, field, report, and job asset imports
- business glossary imports
- lineage edge imports

The sample identifiers are stable and can be referenced in API examples, import templates, or UI acceptance scenarios.
