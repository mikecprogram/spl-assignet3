#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include "../src/Protocol.cpp"


class Client {
private:
    ConnectionHandler connectionHandler;
    Protocol p;
    bool running = true;
public:
    Client(std::string host, short port) : connectionHandler(host, port),
                                           p() {
        std::cout << "Initialized client." << "\n";
    }

    bool connect() {
        if (!connectionHandler.connect()) {
            std::cerr << "Cannot connect to host." << std::endl;
            return false;
        }
        return true;
    }

    void handleWrite() {
        while (running) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            if (!running)
                return;
            std::string line(buf);
            std::vector<char> command = p.commandParser(line);
            int len = command.size();
            if (!connectionHandler.sendBytes(command.data(), len)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            std::cout << "Sent " << len << " bytes to server" << std::endl;
        }
    }

    void handleRead() {
        while (running) {
            std::string msg;
            if (!connectionHandler.getLine(msg)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }

            std::string reply = p.replyParser(msg);
            std::cout << "Reply: " << reply << std::endl << std::endl;
            if (msg == "ACK 4") {
                std::cout << "Exiting...\n" << std::endl;
                running = false;
                break;
            }
        }
    }
};

int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);

    Client c(host, port);
    bool res = c.connect();
    if (!res)
        return 1;

    std::thread th1(&Client::handleRead, &c);
    std::thread th2(&Client::handleWrite, &c);
    th1.join();
    th2.join();

    return 0;
}
