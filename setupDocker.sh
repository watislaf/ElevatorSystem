#bin/bash

#sudo apt-get remove docker docker-engine docker.io containerd runc
#sudo apt-get update
#sudo apt-get install \
#    ca-certificates \
#    curl \
#    gnupg \
#    lsb-release
# curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
# sudo apt-get update
# sudo apt-get install docker-ce docker-ce-cli containerd.io
#  sudo groupadd docker
#sudo usermod -aG docker $USER
# newgrp docker
docker pull gradle
docker stop $( docker container ls | cut -d ' '  -f  1 | tail -1 )
docker build ./ -t elevators
docker run -d -p 6778:6778 elevators  gradle run &