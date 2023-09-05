#!/bin/bash

set -e

declare dc_file="docker/docker-compose.yml"

function build() {
    ./mvnw clean spotless:apply verify
}

function build_image() {
  ./mvnw clean compile spring-boot:build-image -DskipTests -Dspring-boot.build-image.imageName=sivaprasadreddy/bookstore
}

function start_infra() {
    docker-compose -f "${dc_file}" up -d
}

function stop_infra() {
    docker-compose -f "${dc_file}" stop
    docker-compose -f "${dc_file}" rm -f
}

function restart_infra() {
    stop_infra
    sleep 5
    start_infra
}

function start() {
    build_image
    docker-compose --profile app -f "${dc_file}" up --force-recreate -d
}

function stop() {
    docker-compose --profile app -f "${dc_file}" stop
    docker-compose --profile app -f "${dc_file}" rm -f
}

function restart() {
    stop
    sleep 5
    start
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$*
fi

eval "${action}"
