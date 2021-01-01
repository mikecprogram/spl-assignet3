CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude -lthread
LDFLAGS:=-lboost_system -pthread

all: BGRSclient

BGRSclient: bin/connectionHandler.o bin/Protocol.o bin/BGRSClient.o
	g++ -o bin/BGRSclient bin/connectionHandler.o bin/Protocol.o bin/BGRSClient.o $(LDFLAGS)

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGRSClient.o: src/BGRSClient.cpp
	g++ $(CFLAGS) -pthread -o bin/BGRSClient.o src/BGRSClient.cpp

bin/Protocol.o: src/Protocol.cpp
	g++ $(CFLAGS) -o bin/Protocol.o src/Protocol.cpp

#tests

ProtocolTest: bin/Protocol.o bin/ProtocolTest.o
	g++ -o bin/protocolTest bin/Protocol.o bin/ProtocolTest.o

bin/ProtocolTest.o: src/ProtocolTest.cpp
	g++ $(CFLAGS) -o bin/ProtocolTest.o src/ProtocolTest.cpp

clean:
	rm -f bin/*
