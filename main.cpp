#include <iostream>
#include <fstream>
#include <map>
#include "variablestack.h"
#include "sourcefunction.h"
#include "util.h"
#include "instructions.h"

using namespace sos;

std::vector<std::string> readFile();

void parseForFunction(std::vector<std::string> lines);

/**
 *  Makes a map of the functions (They are added by the file parser)
 */
std::map<std::string, void (*)(std::map<std::string, std::string> *, VariableStack *, int *, std::string *)> instructions;

//operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
int main() {

    std::vector<std::string> lines;

    std::cout << "Hello, World!" << std::endl;
    lines = readFile();
    parseForFunction(lines);

    // Deallocate functions
    for(auto & function : Instructions::functions)
        delete function.second;
    return 0;
}

void parseForFunction(std::vector<std::string> lines) {
    std::vector<std::string> params;
    std::string str;
    int i = 0;
    for (std::string str : lines) {
        int startIndex = 0;

        if (str.find("func") == 0) {
            startIndex = i;
            std::cout << str << std::endl;
            params = Util::split(str, ' ');
        }
        if (str.empty()) {
            std::vector<std::string> subParams(params.begin() + 1, params.end());

            Instructions::functions.insert(std::pair(params.at(1), new SourceFunction(&instructions, params.at(1), subParams, &lines, startIndex, i+1)));
        }
        i++;
    }
}

std::vector<std::string>  readFile()
{
    std::vector<std::string> linesImport;

    std::ifstream file("C:\\Users\\Troy.Mullenberg\\Programing\\Workspaces\\GitHub clones\\Sos\\test.txt");
    std::string str;
    while (std::getline(file, str)) {
        linesImport.push_back(str);
    }
    return linesImport;
}


