cmake_minimum_required(VERSION 3.15)
project(sos)

set(CMAKE_CXX_STANDARD 20)

add_executable(sos main.cpp sosc/function.h variablestack.cpp variablestack.h util.cpp util.h instructions.cpp instructions.h sosc/sourcefunction.cpp sosc/sourcefunction.h sosc/nativefunction.cpp sosc/nativefunction.h)