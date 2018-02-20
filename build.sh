#!/bin/sh
./gradlew check
./gradlew build
cp ./build/libs/*.jar app.jar
docker build -t 681283101814.dkr.ecr.ap-southeast-1.amazonaws.com/cashnow-lender-auth-service:$BUILD_NUMBER .
docker push 681283101814.dkr.ecr.ap-southeast-1.amazonaws.com/cashnow-lender-auth-service:$BUILD_NUMBER
docker tag 681283101814.dkr.ecr.ap-southeast-1.amazonaws.com/cashnow-lender-auth-service:$BUILD_NUMBER 681283101814.dkr.ecr.ap-southeast-1.amazonaws.com/cashnow-lender-auth-service:latest
docker push 681283101814.dkr.ecr.ap-southeast-1.amazonaws.com/cashnow-lender-auth-service:latest
rm -rf app.jar
