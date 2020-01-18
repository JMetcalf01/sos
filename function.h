#ifndef SOS_FUNCTION_H
#define SOS_FUNCTION_H

#include <map>
#include <string>
#include <vector>
#include "variablestack.h"

namespace sos {
    class Function {
    private:
        std::map<std::string, void (*) ()>* m_instructions;
        std::string m_name;
        std::vector<std::string> m_params;
        std::string *m_body;
    public:
        Function(std::map<std::string, void (*) ()>* instructions, std::string &name, std::vector<std::string> params, std::vector<std::string>& lines, int start, int end);
        ~Function();

        void execute(VariableStack* stack);
    };
}

#endif //SOS_FUNCTION_H
