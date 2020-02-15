#include <iostream>
#include <fstream>
#include <map>
#include "variablestack.h"
#include "function/sourcefunction.h"
#include "function/nativefunction.h"
#include "util.h"
#include "instructions.h"

using namespace sos;

std::vector<std::string> readFile();

#define DEBUG true

void parseForFunction(std::vector<std::string> lines);

void instruction(const std::string& name, void (*instruction)(std::map<std::string, std::string> *, VariableStack *, int *, std::vector<std::string>));
void native(const std::string& name, void (*function) (VariableStack*));

/**
 *  Makes a map of the functions (They are added by the file parser)
 */
std::map<std::string, void (*)(std::map<std::string, std::string> *, VariableStack *, int *, std::vector<std::string>)> instructions;

//operation.insert(std::pair<std::string, void (*) ()>("This is a test", testFunction));
int main() {
    // Load Instruction Set
    instruction("call", Instructions::call);
    instruction("store", Instructions::store);
    instruction("load", Instructions::load);
    instruction("loadr", Instructions::loadr);
    instruction("read", Instructions::read);
    instruction("goto", Instructions::go);
    instruction("if", Instructions::ifelse);

    // Load Native Functions
    native("🖨", NativeFunction::print);
    native("📄", NativeFunction::printr);
    native("!", NativeFunction::invert);
    native("&&", NativeFunction::boolAnd);
    native("||", NativeFunction::boolOr);
    native("==", NativeFunction::equals);
    native(">", NativeFunction::greater);
    native(">=", NativeFunction::greaterEqual);
    native("<", NativeFunction::less);
    native("<=", NativeFunction::lessEqual);


    std::vector<std::string> lines = readFile();
    parseForFunction(lines);

    if (DEBUG) {
        std::cout << "Using " << instructions.size() << " instructions." << std::endl;
        std::cout << "Loaded " << Instructions::functions.size() << " functions." << std::endl;
    }

    auto* stack = new VariableStack();

    auto main = Instructions::functions.find("main");
    if (main == Instructions::functions.end())
        std::cerr << "Cannot execute file without main function!" << std::endl;
    main->second->execute(stack);

    // Deallocate functions
    for(auto & function : Instructions::functions)
        delete function.second;
    delete stack;
    return 0;
}

void instruction(const std::string& name, void (* instruction)(std::map<std::string, std::string> *, VariableStack *, int *, std::vector<std::string>)) {
    instructions.insert(std::pair<std::string, void (*)(std::map<std::string, std::string> *, VariableStack *, int *, std::vector<std::string>)>(name, instruction));
}

void native(const std::string& name, void (*function) (VariableStack*)) {
    Instructions::functions.insert(std::pair<std::string, Function*>(name, new NativeFunction(function)));
}

void parseForFunction(std::vector<std::string> lines) {
    std::vector<std::string> params;
    std::string str;
    int i = 0;
    int startIndex = 0;
    for (std::string str : lines) {

        if (str.find("func") == 0) {
            startIndex = i + 1;
            params = Util::split(str, ' ');
        }
        if (str.empty()) {
            std::vector<std::string> subParams(params.begin() + 2, params.end());

            Instructions::functions.insert(std::pair(params.at(1), new SourceFunction(&instructions, params.at(1), subParams, &lines, startIndex, i+1)));
        }
        i++;
    }
}

std::vector<std::string>  readFile()
{
    std::vector<std::string> linesImport;

    std::ifstream file("/Users/mattworzala/dev/cpp/sos/test.txt");
//    std::ifstream file("/Users/mattworzala/dev/cpp/sos/example/gamble.🆘🥐");
    std::string str;
    while (std::getline(file, str)) {
        linesImport.push_back(str);
    }
    return linesImport;
}


