
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
    Protocol() = default;
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
            return {};
        }
        std::vector<char> op_arr;
        op_arr.push_back((opcode >> 8) & 0xFF);
        op_arr.push_back(opcode & 0xFF);

        switch(opcode) {
            // case string args
            case 1:
            case 2:
            case 3:
            case 8: {
                args.push_back(0);
                op_arr.insert(op_arr.end(), args.begin(), args.end());
                break;
            }
            case 5:
            case 7:
            case 9:
            case 10: {
                std::string a = std::string(args.begin(), args.end());
                short c = boost::lexical_cast<short>(a);
                op_arr.push_back((c >> 8) & 0xFF);
                op_arr.push_back(c & 0xFF);
                break;
            }
            case 4:
            case 11:
            default:
                break;
        }
        return op_arr;
    }


    short bytesToShort(char bytesArr[])
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

