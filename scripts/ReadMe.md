

# TOC

- [TOC](#toc)
- [Purpose of this repo](#purpose-of-this-repo)
- [Prereqs](#prereqs)
- [Hosts /etc/hosts](#hosts-etchosts)
- [Monitoring](#monitoring)
  - [Prometheus](#prometheus)
  - [Grafana](#grafana)

[Adapted from alainpham's dev-env-scripts](https://github.com/alainpham/dev-env-scripts)

# Purpose of this repo

This is a script to setup what I commonly use in a dev environement to build PoCs. It allows you to mainly build/run docker images on your workstation to avoid having to provision a whole Kubernetes cluster for dev purposes. It requires much less resources than a full blown container platform.

# Prereqs

You should have docker or podman installed with a dedicated.
Create a network called primenet

```
docker network create --driver=bridge --subnet=172.18.0.0/16 --gateway=172.18.0.1 primenet 
```

To get access to Red Hat Enterprise container registry you need to login as follows

```
docker login registry.redhat.io
```

# Hosts /etc/hosts

This is to have some static name resolution docker containers we run locally

```
172.18.0.70 prometheus
172.18.0.71 grafana
```

# Monitoring

## Prometheus

```

docker stop prometheus
docker rm prometheus
docker rmi prom:v2.24.0 

cd prometheus
docker build -t prom:v2.24.0 .
docker run -d --name prometheus --net primenet --ip 172.18.0.70 prom:v2.24.0
cd ..

```




Goto http://prometheus:9090 for admin console

## Grafana

```
docker stop grafana 
docker rm grafana
docker rmi graf:7.3.7

cd grafana
docker build -t graf:7.3.7 . 
docker run -d --name grafana --net primenet --ip 172.18.0.71 graf:7.3.7
cd ..

```

Goto http://grafana:3000 for admin console, login with username/password [admin/admin]