#include <iostream>
#include <fstream>
#include <map>
#include "variablestack.h"
#include "function.h"

using namespace sos;


void testFunction();
std::vector<std::string> readFile(std::string &fileName);



/**
 *  Makes a map of the functions (They are added by the file parser)
 */
std::map<std::string, Function>  functions;
std::vector<std::string> lines;

std::map<std::string, void (*) ()>  instructions;
//operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
int main() {
    std::cout << "Hello, World!" << std::endl;
    lines = readFile((std::string &) "text.txt");
    return 0;
}
void parseForFunction(std::vector<std::string> lines)
{
    std::string str;
    int i = 0;
    for ( std::string str : lines) {
        int startIndex = 0;
        std::vector<std::string> params;
        std::vector<std::string> subParams(params.begin() + 2, params.end());

        if (str.find("func") == 0)
        {
            startIndex = i;
            std::string token = str.substr(0, str.find(" "));
            while ((str.find(" ")) != std::string::npos)
            {
                int pos = str.find(" ");
                token = str.substr(0, pos);
                params.push_back(token);
            }
        }
        if (str.empty())
        {
            functions.insert(std::pair<std::string, Function>(params.at(1), Function(&instructions, params.at(1), subParams, lines, startIndex, i)));
        }
        i++;
    }
}

void testFunction() {
    std::cout << "This is a test function" << std::endl;
}

std::vector<std::string> readFile(std::string &fileName)
{
    std::vector<std::string> linesImport;
    std::ifstream file(fileName);

    for( std::string line; getline( file, line ); )
    {
        linesImport.push_back(line);
    }
    return linesImport;
}


