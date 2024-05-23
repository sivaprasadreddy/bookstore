#!/bin/bash

set -e

declare dc_dir="docker"
declare dc_file="${dc_dir}/docker-compose.yml"
declare dc_monitoring="${dc_dir}/grafana-stack.yml"

function build() {
    ./mvnw clean verify
}

function build_image() {
  ./mvnw -pl bookstore clean compile spring-boot:build-image -DskipTests
}


function start() {
    build_image
    docker compose -f "${dc_file}" up --force-recreate -d
}

function stop() {
    docker compose -f "${dc_file}" stop
    docker compose -f "${dc_file}" rm -f
}

function restart() {
    stop
    sleep 5
    start
}

function start_monitoring() {
    echo 'Starting Grafana Observability Stack....'
    docker-compose -f "${dc_monitoring}" up --build --force-recreate -d
    docker-compose -f "${dc_monitoring}" logs -f
}

function stop_monitoring() {
    echo 'Stopping Grafana Observability Stack....'
    docker-compose -f "${dc_monitoring}" stop
    docker-compose -f "${dc_monitoring}" rm -f
}

function restart_monitoring() {
    stop_monitoring
    sleep 5
    start_monitoring
}

action="start"

if [[ "$#" != "0"  ]]
then
    action=$*
fi

eval "${action}"
