#ifndef SOS_FUNCTION_H
#define SOS_FUNCTION_H

#include <map>
#include <string>
#include <vector>
#include <iostream>
#include "variablestack.h"

namespace sos {
    class Function {
    public:
        virtual void execute(VariableStack* stack){
            throw "UMMMM.....THAT WASN'T SUPOSE TO HAPPEN?!?!?!?!?!";
        }
    };
}

#endif //SOS_FUNCTION_H
