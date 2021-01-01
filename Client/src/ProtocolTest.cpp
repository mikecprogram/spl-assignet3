//
// Created by jules on 30/12/2020.
//

#include "../src/Protocol.cpp"
#include <map>

Protocol p;

void doTest(std::string command){
    auto res = p.commandParser(command);
    std::cout << std::string(res.begin(), res.end()) << "\n";
    std::cout << p.hexRep(res) << "\n";
}


int main(int argc, char *argv[]) {

    while (1) {
        const short bufsize = 1024;
        char buf[bufsize];
        std::cin.getline(buf, bufsize);
        std::string command(buf);

        int len = command.length();
        if (len == 0)
            break;

        doTest(command);

    }
}