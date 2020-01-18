#include "sourcefunction.h"

sos::SourceFunction::SourceFunction(std::map<std::string, void (*)(std::map<std::string, std::string>*, VariableStack*, int*, std::vector<std::string>)> *instructions,
                        std::string &name, std::vector<std::string> &params, std::vector<std::string> *lines, int start, int end) {
    m_instructions = instructions;
    m_name = name;
    m_params = params;
    m_body = new std::vector<std::string>(lines->begin() + start, lines->begin() + end);
}

sos::SourceFunction::~SourceFunction() {
    delete m_body;
}

void sos::SourceFunction::execute(sos::VariableStack *stack) {
    std::map<std::string, std::string> memory;
    for (const std::string& param : m_params)
        memory.insert(std::pair(param, stack->take()));

    for (int i = 0; i < m_body->size(); i++) {
        std::vector<std::string> elements = Util::split(m_body->at(i), ' ');
        std::vector<std::string> params(elements.begin() + 1, elements.end());
        if (elements[0] == "exit")
            return;
        else m_instructions->find(elements[0])->second(&memory, stack, &i, params);
    }
}


