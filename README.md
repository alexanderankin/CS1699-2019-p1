# Project1

## Usage

Make sure you install the [cluster_setup](cluster_setup) before running.

[Docker](https://github.com/docker/docker-install)

```
export ssh_username='your username'
export ssh_password='your password'
curl -fsSL https://get.docker.com -o get-docker.sh
sh get-docker.sh

# maven:3.6-slim

docker run -it --rm --name my-maven-project -p 8080:8080 -v "$PWD":/usr/src/app -v "$HOME/.m2":/root/.m2 -e "ssh_username=$ssh_username" -e "ssh_password=$ssh_password" -w /usr/src/app maven:3.2-jdk-8 mvn clean compile vertx:run
```

## Development

* `cd /path/to/project && mvn clean compile vertx:run`
* `cd /path/to/project/src/main/js/cloudcmp-daa85-p1 && yarn watch`
* `cd /path/to/project/src/main/js/cloudcmp-daa85-p1 && yarn build`
