#!/usr/bin/env bash
mkdir -p services && cd services || return

spring init \
--boot-version=3.4.5 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=integration-service \
--package-name=jsl.group.microservices.integration \
--groupId=jsl.group.microservices.integration \
--dependencies=actuator,web \
--version=1.0.0-SNAPSHOT \
integration-service

spring init \
--boot-version=3.4.5 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=product-service \
--package-name=jsl.group.microservices.core.product \
--groupId=jsl.group.microservices.core.product \
--dependencies=actuator \
--version=1.0.0-SNAPSHOT \
product-service

spring init \
--boot-version=3.4.5 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=order-service \
--package-name=jsl.group.microservices.core.order \
--groupId=jsl.group.microservices.core.order \
--dependencies=actuator,web \
--version=1.0.0-SNAPSHOT \
order-service

spring init \
--boot-version=3.4.5 \
--type=gradle-project \
--java-version=21 \
--packaging=jar \
--name=inventory-service \
--package-name=jsl.group.microservices.core.inventory \
--groupId=jsl.group.microservices.core.inventory \
--dependencies=actuator \
--version=1.0.0-SNAPSHOT \
inventory-service

cd ..

cd services/integration-service && ./gradlew clean build && cd ../..
cd services/product-service && ./gradlew clean build && cd ../..
cd services/order-service && ./gradlew clean build && cd ../..
cd services/inventory-service && ./gradlew clean build && cd ../..

cat <<EOF> settings.gradle
include ':services:product-service'
include ':services:inventory-service'
include ':services:order-service'
include ':services:integration-service'
EOF

cp -r services/product-service/gradle .
cp services/product-service/gradlew .
cp services/product-service/gradlew.bat .
cp services/product-service/.gitignore .

find services -depth -name "gradle" -exec rm -rfv "{}" \;
find services -depth -name "gradlew*" -exec rm -fv "{}" \;

./gradlew build