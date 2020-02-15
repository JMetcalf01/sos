#include "instructions.h"

std::map<std::string, sos::Function*> sos::Instructions::functions;

void sos::Instructions::call(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::vector<std::string> params) {
    auto func = functions.find(params[0]);
    if (func == functions.end())
        throw SosException("Undefined function '" + params[0] + "'!");
    else func->second->execute(stack);
}

void sos::Instructions::store(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::vector<std::string> params) {
    auto curr = memory->find(params[0]);
    if (curr != memory->end())
        memory->erase(curr);

    if (params.size() > 1) {
        std::string value;
        for (int i = 1; i < params.size(); i++)
            value += params[i] + " ";
        memory->insert(std::pair(params[0], value));
    } else memory->insert(std::pair(params[0], stack->take()));
}

void sos::Instructions::load(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::vector<std::string> params) {
    auto value = memory->find(params[0]);
    if (value == memory->end())
        throw SosException("Undefined variable '" + params[0] + "'!");
    else stack->load(value->second);
}

void sos::Instructions::loadr(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::vector<std::string> params) {
    std::string value;
    for (const auto & param : params)
        value += param + " ";
    stack->load(value);
}

void sos::Instructions::read(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::vector<std::string> params) {
    std::string value;
    std::cin >> value;
//    getline(std::cin, value);
    stack->load(value);
}

void sos::Instructions::go(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                           std::vector<std::string> params) {
    *cursor = std::stoi(params[0]) - 1;
}

void sos::Instructions::ifelse(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                               std::vector<std::string> params) {
    int boolean = std::stoi(stack->take());
    if (boolean == 0)
        *cursor += 1;
    *cursor += 0;
}


