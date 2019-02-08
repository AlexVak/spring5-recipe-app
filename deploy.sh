#!/usr/bin/env bash
docker build -t alexvak/spring5-recipe-app:latest -t alexvak/spring5-recipe-app:$SHA -f ./docker-files/recipe-app/Dockerfile .

docker push alexvak/spring5-recipe-app:latest
docker push alexvak/spring5-recipe-app:$SHA

kubectl apply -f k8s

kubectl set image recipe-app-deployment recipe-app=alexvak/spring5-recipe-app:$SHA