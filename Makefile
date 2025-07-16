.DEFAULT_GOAL := build-run

clean:
	make -C app clean
lint:
	make -C app lint
build:
	make -C app build
run:
	make -C app run
test:
	make -C app test
report:
	make -C app report
	
build-run: build run
