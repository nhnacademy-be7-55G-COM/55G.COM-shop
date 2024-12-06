#!/bin/bash

ABSOLUTE_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
profile=$1
container_name="55g-shop-live"
image_name="55g-shop-server"
container_postfix="live"
spring_env="live,docker"
network_bridge="55g-live"
server_port=(8100 8101)

if [ "$profile" == "--dev" ]; then
	container_name="55g-shop-dev"
	spring_env="dev,docker"
	container_postfix="dev"
	network_bridge="55g-dev"
	server_port=(8200 8201)
fi

cd $ABSOLUTE_PATH

docker_network_live_ps=$(docker network ls | grep '55g-live')
if [ -z "$docker_network_live_ps" ]; then
  docker network create 55g-live
fi

docker_network_dev_ps=$(docker network ls | grep '55g-dev')
if [ -z "$docker_network_dev_ps" ]; then
  docker network create 55g-dev
fi

echo "Building docker image..."
docker build -t $image_name-$container_postfix .

for ((i=0; i<${#server_port[@]}; i++)); do
    target_port=${server_port[i]}

    curl -X POST http://localhost:$target_port/actuator/status
    sleep 30;

    echo "Removing container $container_name-$i..."
    docker stop $container_name-$i
    docker rm $container_name-$i

    echo "Creating container for service..."
    docker run -d --name $container_name-$i \
           --network $network_bridge \
           --env SPRING_PROFILE=$spring_env \
           --env SERVER_PORT=$target_port \
           -p $target_port:$target_port \
           -v /logs:/logs \
           -v /var/55g/static:/static \
           $image_name-$container_postfix

     until $(curl --output /dev/null --silent --head --fail http://localhost:$target_port/actuator/health); do
         echo "Waiting for the application to be ready..."
         sleep 5
     done
done

echo "Pruning images..."
docker image prune --force
