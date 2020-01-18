#include "nativefunction.h"

void sos::NativeFunction::print(sos::VariableStack *stack) {
    std::cout << stack->take() << std::endl;
}

void sos::NativeFunction::invert(VariableStack* stack) {
    stack->load(stack->take() == "0");
}

void sos::NativeFunction::boolAnd(sos::VariableStack *stack) {
    stack->load(std::stoi(stack->take()) == std::stoi(stack->take()) == 1);
}

void sos::NativeFunction::boolOr(sos::VariableStack *stack) {
    int left = std::stoi(stack->take());
    int right = std::stoi(stack->take());
    stack->load(left == 1 || right == 1);
}

void sos::NativeFunction::equals(sos::VariableStack *stack) {
    stack->load(stack->take() == stack->take());
}

void sos::NativeFunction::greater(sos::VariableStack *stack) {
    int left = std::stoi(stack->take());
    int right = std::stoi(stack->take());
    stack->load(left > right);
}

void sos::NativeFunction::greaterEqual(sos::VariableStack *stack) {
    int left = std::stoi(stack->take());
    int right = std::stoi(stack->take());
    stack->load(left >= right);
}

void sos::NativeFunction::less(sos::VariableStack *stack) {
    int left = std::stoi(stack->take());
    int right = std::stoi(stack->take());
    stack->load(left < right);
}

void sos::NativeFunction::lessEqual(sos::VariableStack *stack) {
    int left = std::stoi(stack->take());
    int right = std::stoi(stack->take());
    stack->load(left <= right);
}
