# FF Intg

## Quick Start

- Setup a `.env` file from `.env.template`
- Requires rabbitmq and postgres to be running locally or elsewhere
- `gradle bootrun` to run the server
- `gradle flywayMigrate` to migrate via cli

### Rabbitmq Setup

- optionally can be done in vhost
- requires exchange `invoices_exchange`
- requires queue `invoices_queue` bound to exchange with routing key `*`

### Postgres Helpers

`dropdb <dbname>`
`createdb --username=<username> --owner=<username> <dbname>`