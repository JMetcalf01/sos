//
// Created by Troy.Mullenberg on 1/18/2020.
//

#ifndef SOS_NATIVEFUNCTION_H
#define SOS_NATIVEFUNCTION_H

#include "function.h"

namespace sos {
    class NativeFunction : public Function {
    private:
        void (*function) (VariableStack *stack);
    public:
         NativeFunction(void (*)(VariableStack *stack));

        void execute(VariableStack *stack) override;

    };
}

#endif //SOS_NATIVEFUNCTION_H
