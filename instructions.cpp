#include <map>
#include "variablestack.h"
#include "function.h"

namespace sos {
    std::map<std::string, Function> functions;

    //void TEMPLATE(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params) {}

    // call FUNC_NAME
    // Call a named function. The parameters must be loaded to the "stack" using `load` or `loadr` before call.
    void call(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params) {
        auto func = functions.find(params[0]);
        if (func == functions.end())
            throw "UNKNOWN FUNCTION HAS BEEN CALLED, THIS IS AN ERROR!";
        else func->second.execute(stack);
    }

    // store VAR_NAME VALUE
    // Assign a name to a value. This may be used multiple times to reassign.
    void store(std::map<std::string, std::string>* memory, VariableStack* stack, int* cursor, std::string* params) {
        memory->insert(std::pair(params[0], params[1]));
    }
}
