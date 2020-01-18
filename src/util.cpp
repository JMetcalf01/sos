#include "util.h"

std::vector<std::string> sos::Util::split(std::string str, char delimiter) {
    std::vector<std::string> elements;

    size_t pos = 0;
    while ((pos = str.find(delimiter)) != std::string::npos) {
        elements.push_back(str.substr(0, pos));
        str.erase(0, pos + 1);
    }
    elements.push_back(str);
    return elements;
}

std::string* sos::Util::subvector(std::vector<std::string> *vec, int start, int end) {
    auto* sub = new std::string[end - start];
    for (int i = start; i < end; i++)
        sub[i - start] = vec->at(i);
    return sub;
}
