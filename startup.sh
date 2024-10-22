#!/bin/bash

ABSOLUTE_PATH="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
profile=$1
container_name="55g-shop-live"
image_name="55g-shop-server"
container_postfix="live"
spring_env="live,docker"
server_port=8100

if [ "$profile" == "--dev" ]; then
	container_name="55g-shop-dev"
	spring_env="dev,docker"
	container_postfix="dev"
	server_port=8200
fi

cd $ABSOLUTE_PATH

docker_ps=$(docker ps --all --filter "name=${container_name}" | awk '{ print $1 }')

i=0
for line in $docker_ps; do
  ps_arr[i]=$line
  i=$((i+1))
done

for ((i=1; i<${#ps_arr[@]}; i++)); do
    echo "Removing container ${ps_arr[i]}..."
    docker stop ${ps_arr[i]}
    docker rm ${ps_arr[i]}
done

echo "Building docker image..."
docker build -t $image_name-$container_postfix .

echo "Creating container for service..."
docker run -d --name $container_name --env SPRING_PROFILE=$spring_env --env SERVER_PORT=$server_port -p $server_port:$server_port $image_name-$container_postfix

echo "Pruning images..."
docker image prune --force
