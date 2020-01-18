#include "variablestack.h"

void sos::VariableStack::load(std::string item) {
    items.push_back(item);
}

std::string sos::VariableStack::take() {
    if (items.empty())
        throw SosException("Cannot take an item from an empty queue!");
    auto front = items.front();
    items.erase(items.begin());
    return front;
}

void sos::VariableStack::clear() {
    items.clear();
}

void sos::VariableStack::load(bool item) {
    if (item)
        load((std::string) "1");
    else load((std::string) "0");
}
