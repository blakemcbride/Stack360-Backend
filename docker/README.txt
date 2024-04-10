Stack360 Docker

-----

Building the images

Assumes ~/intellijProjects/Stack360-backend.tar.gz exists
Assumes ~/intellijProjects/Stack360-frontend.tar.gz exists

The above two files are created by tar’ing up:
	~/intellijProjects/Stack360-backend
~/intellijProjects/Stack360-frontend

Those two directories are created by the ../make-open-source-version scripts in both projects

cd ~/intellijProjects/Stack360Backend/docker
./build-demo-image
./build-prod-image

---------------------------------------------------------

Running the image


docker run -it -p 8001:8001 -p 8080:8080 blake1024/stack360:demo.1
       or
docker run -it -p 8001:8001 -p 8080:8080 blake1024/stack360:prod.1

^C


---------------------------

Attach to a running container

exec -it <container-id> /bin/bash

-------------------------------------------

Push the image

docker login
docker push blake1024/stack360:demo.1



-----------------------------------------------------------------


docker ps -a
docker start -i XXXX   (or if already running, docker attach XXX)
service postgresql start

