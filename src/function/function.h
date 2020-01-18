#ifndef SOS_FUNCTION_H
#define SOS_FUNCTION_H

#include <map>
#include <string>
#include <vector>
#include <iostream>
#include "../variablestack.h"

namespace sos {
    class Function {
    public:
        virtual void execute(VariableStack* stack) {
            throw std::runtime_error("An unknown error has occurred!");
        }
    };
}

#endif //SOS_FUNCTION_H
