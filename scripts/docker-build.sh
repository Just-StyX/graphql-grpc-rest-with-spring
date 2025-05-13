#!/usr/bin/env bash

./gradlew build && docker compose build && docker compose up -d