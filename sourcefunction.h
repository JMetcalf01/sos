//
// Created by Troy.Mullenberg on 1/18/2020.
//

#ifndef SOS_SOURCEFUNCTION_H
#define SOS_SOURCEFUNCTION_H

#include <map>
#include <string>
#include <vector>
#include "variablestack.h"
#include "function.h"

namespace  sos
{
    class SourceFunction : public Function {
        private:
            std::map<std::string, void (*)(std::map<std::string, std::string>*, VariableStack*, int*, std::string*)> *m_instructions;
            std::string m_name;
            std::vector<std::string> m_params;
            std::string *m_body;
        public:
            SourceFunction(std::map<std::string, void (*)(std::map<std::string, std::string> *, VariableStack *, int *, std::string*)> *instructions,
                     std::string &name, std::vector<std::string> &params, std::vector<std::string> *lines, int start, int end);
            ~SourceFunction();
    };
}

#endif //SOS_SOURCEFUNCTION_H
