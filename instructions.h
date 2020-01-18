#ifndef SOS_INSTRUCTIONS_H
#define SOS_INSTRUCTIONS_H

#include <map>
#include "variablestack.h"
#include "function/function.h"
#include <iostream>

namespace sos {
    class Instructions {
    public:
        static std::map<std::string, Function*> functions;

        // call FUNC_NAME
        // Call a named function. The parameters must be loaded to the "stack" using `load` or `loadr` before call.
        static void call(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);

        // store VAR_NAME VALUE
        // Assign a name to a value. This may be used multiple times to reassign.
        static void store(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);

        // load VAR_NAME
        // Load a variable by name to the "stack"
        static void load(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);

        // loadr VALUE
        // Load a raw value to the "stack"
        static void loadr(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);

        // read VAR_NAME
        // Reads a line of user input and assigns the value to the variable.
        static void read(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);

        // goto LINE_NUMBER
        // Go to a defined position (it may be defined after the goto, and will be searched).
        static void go(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params);
    };
}


#endif //SOS_INSTRUCTIONS_H
