#bin/bash


docker stop $( docker container ls | cut -d ' '  -f  1 | tail -1 )
docker build ./ -t elevators
docker run -d -p 6778:6778 elevators  gradle run &