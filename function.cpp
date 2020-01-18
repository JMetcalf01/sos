#include "function.h"
#include "main.cpp"

sos::Function::Function(std::map<std::string, void (*)(std::map<std::string, std::string>*, VariableStack*, int*, std::string&)> *instructions,
        std::string &name, std::vector<std::string> &params, std::vector<std::string> &lines, int start, int end) {
    m_instructions = instructions;
    m_name = name;
    m_params = params;
    m_body = new std::string[end - start];

    for (int i = start; i < end; i++)
        m_body[i - start] = lines[i];
}

sos::Function::~Function() {
    delete[] m_body;
}

void sos::Function::execute(sos::VariableStack *stack) {
    std::map<std::string, std::string> memory;
    for (const std::string& param : m_params)
        memory.insert(std::pair(param, stack->take()));

    for (int i = 0; i < m_body->size(); i++) {

        m_instructions->find("")->second(&memory, stack, &i);
    }
}
