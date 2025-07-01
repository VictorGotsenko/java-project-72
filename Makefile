.PHONY: build
.DEFAULT_GOAL := build-run

clean:
	make -C app clean
lint:
	make -C app lint
build:
	make -C app build
install:
	make -C app install	
run:
	make -C app run
run-dist:
	make -C app run-dist
test:
	make -C app test
report:
	make -C app report
check-deps:
	make -C app check-deps
update-deps:
	make -C app update-deps
	
build-run: build run
