#include "function.h"

sos::Function::Function(std::string &name, std::map<std::string, void (*)()> *instructions, std::vector<std::string> &lines, int start, int end) {
    m_name = name;
    m_instructions = instructions;
    m_body = new std::string[end - start];

    for (int i = start; i < end; i++)
        m_body[i - start] = lines[i];
}

sos::Function::~Function() {
    delete[] m_body;
}

void sos::Function::execute(sos::VariableStack *stack) {
    //todo
}
