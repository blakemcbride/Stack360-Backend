#!/bin/bash

# Start PostgreSQL
pg_ctlcluster 14 main start

# Give PostgreSQL some time to start
sleep 2

# Start the back-end
cd /home/Stack360-backend
./bld run

# Start the front-end
cd ../Stack360-frontend
./serve &

# Show back-end system log
cd ../Stack360-backend
./view-log

# Keep the container running - drop into a Bash shell
# exec bash
