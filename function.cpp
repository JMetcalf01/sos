#include "function.h"
#include "util.h"

sos::Function::Function(std::map<std::string, void (*)(std::map<std::string, std::string>*, VariableStack*, int*, std::string*)> *instructions,
        std::string &name, std::vector<std::string> &params, std::vector<std::string> *lines, int start, int end) {
    m_instructions = instructions;
    m_name = name;
    m_params = params;
    m_body = Util::subvector(lines, start, end);

}

sos::Function::~Function() {
    delete[] m_body;
}

void sos::Function::execute(sos::VariableStack *stack) {
    std::map<std::string, std::string> memory;
    for (const std::string& param : m_params)
        memory.insert(std::pair(param, stack->take()));

    for (int i = 0; i < m_body->size(); i++) {
        std::vector<std::string> elements = Util::split(m_body[i], ' ');
        std::string* params = Util::subvector(&elements, 1, elements.size());
        if (elements[0] == "exit")
            return;
        else m_instructions->find(elements[0])->second(&memory, stack, &i, params);
        delete[] params;
    }
}
