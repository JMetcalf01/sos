#include "instructions.h"

void sos::Instructions::call(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {
    auto func = functions.find(params[0]);
    if (func == functions.end())
        throw "UNKNOWN FUNCTION HAS BEEN CALLED, THIS IS AN ERROR!";
    else func->second.execute(stack);
}

void sos::Instructions::store(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::string *params) {
    memory->insert(std::pair(params[0], params[1]));
}

void sos::Instructions::load(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {

}

void sos::Instructions::loadr(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                              std::string *params) {

}

void sos::Instructions::read(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {

}

void sos::Instructions::go(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                           std::string *params) {

}

void sos::Instructions::exit(std::map<std::string, std::string> *memory, sos::VariableStack *stack, int *cursor,
                             std::string *params) {

}
