#!/bin/sh

#set -x

count=0
while [ ${count} -eq 0 ]; do
  count=$(ssh -o StrictHostKeyChecking=no -o PubkeyAuthentication=no -o PasswordAuthentication=no -o KbdInteractiveAuthentication=no -o ChallengeResponseAuthentication=no localhost -p 2222 2>&1 | grep denied | wc -l)
  echo waiting 5 seconds until oracle is up...
sleep 5
done
