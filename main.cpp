#include <iostream>
#include "variablestack.h"

using namespace sos;

int main() {
    VariableStack stack;

    stack.load("Hello, World");



    std::cout << stack.take() << std::endl;

    stack.take();

    return 0;
}
