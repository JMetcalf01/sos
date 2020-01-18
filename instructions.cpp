#include "instructions.h"

std::map<std::string, sos::Function*> sos::Instructions::functions;

void sos::Instructions::call(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {
    auto func = functions.find(params[0]);
    if (func == functions.end())
        throw "UNKNOWN FUNCTION HAS BEEN CALLED, THIS IS AN ERROR!";
    else func->second->execute(stack);
}

void sos::Instructions::store(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::string *params) {
    memory->insert(std::pair(params[0], params[1]));
}

void sos::Instructions::load(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {
    auto value = memory->find(params[0]);
    if (value == memory->end())
        throw "Undefined variable: " + params[0];
    else stack->load(value->second);
}

void sos::Instructions::loadr(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::string *params) {
    std::string value;
    for (int i = 1; i < params->size(); i++)
        value += params[i];
    stack->load(value);
}

void sos::Instructions::read(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {
    std::string value;
    std::cin >> value;
    stack->load(value);
}

void sos::Instructions::go(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                           std::string *params) {
    *cursor = std::stoi(params[0]) - 1;
}
