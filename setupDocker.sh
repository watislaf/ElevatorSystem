#bin/bash

if [[ $( docker container ls | cut -d ' '  -f  1 | tail -1 ) != "CONTAINER" ]]; then
  docker stop $( docker container ls | cut -d ' '  -f  1 | tail -1 )
  docker rm $( ps -a | cut -d ' '  -f  1 | tail -1 )
fi;

docker build ./ -t elevators
docker run -d -p 6778:6778 elevators  gradle run &