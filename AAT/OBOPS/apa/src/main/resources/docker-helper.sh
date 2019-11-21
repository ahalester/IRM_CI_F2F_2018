# source this in your ~/.bash_profile
# for mac os x

alias d=docker
alias dm=docker-machine

###############  DOCKER MACHINE vm and it's env

export DOCKER_MACHINE_NAME=dev

# create vm and set up shell env
function d-machine-create(){
  docker-machine create --driver virtualbox $DOCKER_MACHINE_NAME
  d-env
}

# shell env for machine
function d-env(){
  echo "DOCKER_MACHINE_NAME=$DOCKER_MACHINE_NAME"
  eval "$(docker-machine env $DOCKER_MACHINE_NAME)"
  docker info
  echo "*** ALL RUNNING CONTAINERS"
  docker ps  -s
}

# up machine
function d-up(){
  docker-machine start $DOCKER_MACHINE_NAME
  d-sh
}

# down machine
function d-down(){
  docker-machine stop $DOCKER_MACHINE_NAME
}

function d-ssh(){
  docker-machine ssh $DOCKER_MACHINE_NAME
}

############### CONTAINERS AND IMAGES

# remove stopped container and untagged images
function d-clean(){
  docker ps -qa --filter status=exited | xargs -L1 docker rm
  docker images --filter dangling=true -q | xargs -L1 docker rmi
}

# stop all running containers
function d-stop(){
  docker ps -q | xargs -L1 docker stop
}

# remove all images
function d-nuke(){
  docker images -aq | xargs -L1 docker rmi -f
}