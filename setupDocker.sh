#bin/bash

sudo apt-get remove docker docker-engine docker.io containerd runc
sudo apt-get update
sudo apt-get install \
    ca-certificates \
    curl \
    gnupg \
    lsb-release
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
udo apt-get update
sudo apt-get install docker-ce docker-ce-cli containerd.io

docker pull gradle

docker build ./ -t elevators
docker run -b -p 6778:6778 elevators  gradle run