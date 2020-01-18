#include <iostream>
#include <fstream>
#include <map>
#include "variablestack.h"

using namespace sos;

void testFunction();

int main() {
    std::cout << "Hello, World!" << std::endl;


    return 0;
}
void parserLine(std::ifstream file)
{

    VariableStack stack;
    std::string str;
    while (getline(file, str)) {

        std::map<std::string, void (*) ()>  operation;
        operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
// map (string, functionObject)


    }
}

void testFunction() {
    std::cout << "This is a test function" << std::endl;
}

std::ifstream openFile(std::string fileName)
{
    std::ifstream file(fileName);
    return file;
}


