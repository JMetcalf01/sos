#include "variablestack.h"

void sos::VariableStack::load(std::string item) {
    items.push_back(item);
}

std::string sos::VariableStack::take() {
    if (items.empty())
        throw "CANNOT TAKE FROM EMPTY STACK";
    auto front = items.front();
    items.erase(items.begin());
    return front;
}

void sos::VariableStack::clear() {
    items.clear();
}
