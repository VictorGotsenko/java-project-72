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
report:
	./gradlew jacocoTestReport	

build-run: build run
