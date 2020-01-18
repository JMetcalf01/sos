#include <iostream>
#include <fstream>
#include <map>
#include <string>
#include "variablestack.h"
#include "function.h"

using namespace sos;


void testFunction();
std::vector<std::string> readFile();
void parseForFunction(std::vector<std::string> lines);
std::vector<std::string> split(std::string str, char delimiter);


/**
 *  Makes a map of the functions (They are added by the file parser)
 */
std::map<std::string, Function>  functions;
std::map<std::string, void (*) ()>  instructions;
//operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
int main() {
    std::vector<std::string> lines;

    std::cout << "Hello, World!" << std::endl;
    lines  = readFile();
    std::cout << "testing" << std::endl;
    std::cout << lines.at(0) << std::endl;
    parseForFunction(lines);
    return 0;
}
void parseForFunction(std::vector<std::string> lines)
{
    std::vector<std::string> params;
    std::string str;
    int i = 0;
    for ( std::string str : lines) {
        int startIndex = 0;

        if (str.find("func") == 0)
        {
            startIndex = i;
//            std::string token = str.substr(0, str.find(" "));
//            while ((str.find(" ")) != std::string::npos)
//            {
//                int pos = str.find(" ");
//                std::string token = str.substr(0, pos);
//                params.push_back(token);
//            }
            std::string word = "";
            for (auto x : str)
            {
                if (x == ' ')
                {
                    std::cout << word << std::endl;
                    params.push_back(word);
                    word = "";
                }
                else
                {
                    word = word + x;
                }
            }
            std::cout << word << std::endl;
        }
        if (str.empty())
        {
            std::vector<std::string> subParams(params.begin() + 1, params.end());
            functions.insert(std::pair<std::string, Function>(params.at(1), Function(&instructions, params.at(1), subParams, lines, startIndex, i + 1)));
        }
        i++;
    }
}

std::vector<std::string> split(std::string str, char delimiter) {
    std::vector<std::string> elements;

    size_t pos = 0;
    while ((pos = str.find(delimiter)) != std::string::npos) {
        elements.push_back(str.substr(0, pos));
        str.erase(0, pos);
    }

    return elements;
}

void testFunction() {
    std::cout << "This is a test function" << std::endl;
}

std::vector<std::string>  readFile()
{
    std::vector<std::string> linesImport;

    std::ifstream file("ToDebug...");
    std::string str;
    while (std::getline(file, str))
    {
        std::cout << str << "\n";
        linesImport.push_back(str);
    }
    return linesImport;
}


