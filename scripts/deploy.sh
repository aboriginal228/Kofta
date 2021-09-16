#!/usr/bin/env bash

cd ../

mvn clean package

echo 'Copy files...'

ls

scp target/kofta-1.0-SNAPSHOT.jar \
    root@192.168.0.13:/home/kofta/

echo 'Restart server...'

ssh root@192.168.0.13 <<EOF

nohup java -jar /home/kofta/kofta-1.0-SNAPSHOT.jar > /home/kofta/log.txt &

EOF

echo 'Bye'
