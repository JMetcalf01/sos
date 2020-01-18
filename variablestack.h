#ifndef SOS_VARIABLESTACK_H
#define SOS_VARIABLESTACK_H

#include <vector>
#include <string>

namespace sos {
    class VariableStack {
    private:
        std::vector<std::string> items;
    public:
        void load(std::string item);
        std::string take();
        void clear();
    };
}


#endif //SOS_VARIABLESTACK_H
