#include <iostream>
#include <fstream>
#include <map>

void testFunction();

using namespace std;

int main() {
    std::cout << "Hello, World!" << std::endl;


    return 0;
}
void parserLine(ifstream file)
{
    string str;
    while (getline(file, str)) {

        std::map<std::string, void (*) ()>  operation;
        operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
// map (string, functionObject)


    }
}

void testFunction() {
    std::cout << "This is a test function" << std::endl;
}

ifstream openFile(string fileName)
{
    ifstream file(fileName);
    return file;
}


