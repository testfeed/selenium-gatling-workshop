### Tooling
- to be able to inspect the code: openjdk 8 and IntelliJ with the Scala plugin
- to run the project: sbt and docker
- all of the following instructions have been tested on an Ubuntu 18.04 installation

#### Install openjdk 8
```
    sudo apt install openjdk-8-jdk
```

#### Install sbt (https://www.scala-sbt.org/1.0/docs/Installing-sbt-on-Linux.html)
```
    echo "deb https://dl.bintray.com/sbt/debian /" | sudo tee -a /etc/apt/sources.list.d/sbt.list
    sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv 2EE0EA64E40A89B84B2DF73499E82A75642AC823
    sudo apt-get update
    sudo apt-get install sbt
```

#### Install docker (https://docs.docker.com/install/linux/docker-ce/ubuntu/)

```
    sudo apt-get install \
        apt-transport-https \
        ca-certificates \
        curl \
        gnupg-agent \
        software-properties-common

    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -

    sudo apt-key fingerprint 0EBFCD88

    sudo add-apt-repository \
    "deb [arch=amd64] https://download.docker.com/linux/ubuntu \
    $(lsb_release -cs) \
    stable"

    sudo apt-get update

    sudo apt-get install docker-ce docker-ce-cli containerd.io
```

#### Install IntelliJ Community Edition
- download Community Edition from https://www.jetbrains.com/idea/download/#section=linux
```
    tar -xzvf ideaIC-2019.1.3.tar.gz
    cd idea-IC-191.7479.19
    ./bin/idea.sh

```
- to install, accept the license
- on the "Download featured plugins" section Install the Scala plugin

### Run the workshop project
- this ensures all dependencies are resolved locally and the docker image is also cached locally

#### Download the Mozilla geckodriver
- https://github.com/mozilla/geckodriver/releases/download/v0.24.0/geckodriver-v0.24.0-linux64.tar.gz
- untar it and make it executable
```
    tar -xzvf geckodriver-v0.24.0-linux64.tar.gz -C /home/$USER/
    chmod +x /home/$USER/geckodriver
```

### Start the OWASP juice shop
```
    sudo docker run --rm -p 3000:3000 bkimminich/juice-shop:v8.6.2
```

#### Clone the workshop git repo
```
    git clone git@github.com:testfeed/selenium-gatling-workshop.git
```

#### Resolve the selenium project dependencies and run the sample test
```
    cd selenium-gatling-workshop/selenium
    sbt -Dwebdriver.gecko.driver=/home/$USER/geckodriver test

```
- the test might fail at the moment

#### Resolve the gatling project dependencies and run the sample test
```
    cd selenium-gatling-workshop/gatling
    sbt "gatling:test"
```
- the test might fail at the moment
