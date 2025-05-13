#!/usr/bin/env bash

kubectl -n questions create secret generic postgresql \
  --from-literal POSTGRES_USER="username" \
  --from-literal POSTGRES_PASSWORD='password' \
  --from-literal POSTGRES_DB="questions-db" \
  --from-literal REPLICATION_USER="username" \
  --from-literal REPLICATION_PASSWORD='password'