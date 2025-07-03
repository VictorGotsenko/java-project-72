.PHONY: build
.DEFAULT_GOAL := build-run

clean:
	./gradlew clean
lint:
	./gradlew checkstyleMain
build:
	./gradlew clean build
run:
	./gradlew run	
test:
	./gradlew test

build-run: build run
