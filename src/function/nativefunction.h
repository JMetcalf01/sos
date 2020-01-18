#ifndef SOS_NATIVEFUNCTION_H
#define SOS_NATIVEFUNCTION_H

#include <iostream>
#include "function.h"

namespace sos {
    class NativeFunction : public Function {
    private:
        void (*m_function) (VariableStack*);
    public:
        NativeFunction(void (*function)(VariableStack*)) { m_function = function; }
        void execute(VariableStack *stack) override { m_function(stack); }

        static void print(VariableStack* stack) {
            std::cout << stack->take() << std::endl;
        }
    };
}

#endif //SOS_NATIVEFUNCTION_H
