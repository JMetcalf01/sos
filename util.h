#ifndef SOS_UTIL_H
#define SOS_UTIL_H

#include <string>
#include <vector>

namespace sos {
    class Util {
    public:
        static std::vector<std::string> split(std::string str, char delimiter);
        static std::string* subvector(std::vector<std::string>* vec, int start, int end);
    };
}



#endif //SOS_UTIL_H
