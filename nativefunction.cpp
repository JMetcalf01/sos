//
// Created by Troy.Mullenberg on 1/18/2020.
//

#include "nativefunction.h"

void sos::NativeFunction::execute(sos::VariableStack *stack) {
    Function::execute(stack);
}

sos::NativeFunction::NativeFunction(void (*)(VariableStack *)) {

}
