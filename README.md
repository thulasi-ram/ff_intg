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

### Invoice Producer and Consumer
- InvoicePublisher produces simple invoice payload periodically n seconds
- InvoiceConsumer consumes them as soon as it is published.
- todo: Currently immediate ack and prefetch is done which needs to be stopped
- The stored json triggeres another camel route which starts a temporal workflow

## Docker
`docker-compose up --build -d`

- the sql.yaml file below needs to be in a directory `dynamicconfig` under root 
  - https://github.com/temporalio/docker-compose/blob/main/dynamicconfig/development-sql.yaml
- [ ] rabbitmq definitions to create vhost and exchange bindings