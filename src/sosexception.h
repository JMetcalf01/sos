#ifndef SOS_SOSEXCEPTION_H
#define SOS_SOSEXCEPTION_H

#include <exception>
#include <string>

class SosException : public std::exception {
private:
    std::string m_message;
public:
    explicit SosException(const std::string& message) { m_message = message; }
    virtual const char* what() const throw() {
        return m_message.c_str();
    }
};

#endif //SOS_SOSEXCEPTION_H
