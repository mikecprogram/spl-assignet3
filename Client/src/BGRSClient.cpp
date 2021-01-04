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
        std::cout << "Connected." << std::endl;
        return true;
    }

    void handleWrite() {
        while (running) {
            const short bufsize = 1024;
            char buf[bufsize];
            std::cin.getline(buf, bufsize);
            std::string line(buf);
            std::vector<char> command = p.commandParser(line);
            int len = command.size();
            if (len > 0) {
                if (!connectionHandler.sendBytes(command.data(), len)) {
                    std::cout << "Disconnected. Exiting...\n" << std::endl;
                    break;
                }
            }
        }
    }

    void handleRead() {
        while (running) {
            char repArr[2] = {'\0', '\0'};
            char opArr[2] = {'\0', '\0'};
            if (!connectionHandler.getBytes(repArr, 2)) {
                std::cout << "Disconnected. Exiting...\n" << std::endl;
                break;
            }
            connectionHandler.getBytes(opArr, 2);
            short rep = p.bytesToShort(repArr);
            short op = p.bytesToShort(opArr);

            // ack
            std::string msg = "";
            if (rep == 12){
                switch(op){
                    case 1: case 2: case 3: case 5: case 10:
                    default:
                        std::cout << "ACK " << op << "\n";
                        break;
                    case 6: case 7: case 8: case 9: case 11:
                        if (!connectionHandler.getFrameAscii(msg, '\0'))
                            // (if it fails it probably goes into endless loop anyway)
                            std::cout << "Failed to read message.\n";
                        else
                            std::cout << "ACK " << op << "\n" << msg << "\n";
                        break;
                    case 4:
                        std::cout << "ACK 4\nExiting...\n" << std::endl;
                        running = false;
                        std::exit(0);
                }

            }
            // error
            if (rep == 13)
                std::cout << "ERROR " << op << "\n";
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
