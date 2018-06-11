all:
	icc -qopenmp -o sherlock Sherlock.c

clean:
	rm -f hello
