
#include <vector>
#include <iostream>
#include <map>
#include <iomanip>
#include <boost/lexical_cast.hpp>

class Protocol {
private:
    std::map<std::string,short> op_codes = {
            {"ADMINREG", 1}, {"STUDENTREG", 2}, {"LOGIN", 3},
            {"LOGOUT", 4}, {"COURSEREG", 5}, {"KDAMCHECK", 6},
            {"COURSESTAT", 7}, {"STUDENTSTAT", 8},
            {"ISREGISTERED", 9}, {"UNREGISTER", 10},
            {"MYCOURSES", 11}, {"ACK", 12}, {"ERR", 13}};


public:
    Protocol() {}
    std::vector<char> commandParser(std::string content) {
        short opcode = 0;
        std::vector<char> op;
        std::vector<char> args;

        for(unsigned i=0; i < content.length(); i++){
            if (!opcode)
            {
                if (content[i] == ' '){
                    opcode = 1;
                }
                else
                    op.push_back(content[i]);
            }
            else {
                if (content[i] == ' ')
                    args.push_back(0);
                else
                    args.push_back(content[i]);
            }
        }
        // convert op code
        opcode = op_codes[std::string(op.begin(), op.end())];
        if (opcode == 0) {
            std::cerr << "Invalid operation: " << std::string(op.begin(), op.end()) << "\n";
            return op;
        }
        std::vector<char> op_arr;
        op_arr.push_back((opcode >> 8) & 0xFF);
        op_arr.push_back(opcode & 0xFF);

        switch(opcode) {
            // case string args
            case 1:
            case 2:
            case 3:
            case 8:
                args.push_back(0);
                op_arr.insert(op_arr.end(), args.begin(), args.end());
                break;
            case 4:
                break;
            default:
                std::string a = std::string(args.begin(), args.end());
                short c = boost::lexical_cast<short>(a);
                op_arr.push_back((c >> 8) & 0xFF);
                op_arr.push_back(c & 0xFF);
                break;
        }
        return op_arr;
    }

    std::string replyParser(std::string content){
        short result = bytesToShort(content.substr(0, 2));
        short op = bytesToShort(content.substr(2, 4));

        std::stringstream ss;;
        if (result == 12)
            ss << "ACK ";
        if (result == 13)
            ss << "ERROR ";
        ss << op << " ";
        ss << content.substr(4);
        return ss.str();
    }

    short bytesToShort(std::string bytesArr)
    {
        short result = (short)((bytesArr[0] & 0xff) << 8);
        result += (short)(bytesArr[1] & 0xff);
        return result;
    }

    std::string hexRep(std::vector<char> s){
        std::stringstream ss;
        ss << std::hex << std::setfill('0');
        for(unsigned i=0; i<s.size(); ++i)
            ss << std::setw(2) << static_cast<unsigned>(s[i]) << ' ';
        return ss.str();
    }
};

